# Deploy e Infraestrutura

Como o Workshop Management System é conteinerizado, orquestrado e entregue continuamente — Docker, Kubernetes, Terraform e o pipeline de CI/CD.

## Infraestrutura

Topologia do cluster provisionado (namespace, ConfigMaps/Secrets, Deployments, Services, HPA e o add-on de métricas):

```mermaid
flowchart TB
    classDef external fill:#999999,stroke:#6b6b6b,color:#fff
    classDef node fill:#e8e8e8,stroke:#888,color:#000
    classDef deploy fill:#438dd5,stroke:#2e6295,color:#fff
    classDef svc fill:#85bbf0,stroke:#5d82a8,color:#000
    classDef store fill:#ffd580,stroke:#b38600,color:#000

    DockerHub["Docker Hub<br/>[Sistema Externo]<br/>adrianameyer/workshop-management<br/>tags: latest, sha"]:::external

    subgraph Kind["Kind Cluster 'workshop' (Docker-in-Docker no runner)"]
      direction TB
      subgraph NS["Namespace: workshop"]
        direction TB

        subgraph Config["ConfigMaps / Secrets"]
          direction LR
          CM1["app-config"]:::store
          CM2["mysql-config"]:::store
          SEC1["app-secret<br/>JWT_SECRET, WEBHOOK_TOKEN,<br/>SPRING_DATASOURCE_*"]:::store
          SEC2["mysql-secret<br/>MYSQL_ROOT_PASSWORD,<br/>MYSQL_PASSWORD"]:::store
          SEC3["dockerhub-secret<br/>imagePullSecret"]:::store
        end

        DeployApp["Deployment: workshop-app<br/>1 réplica (HPA: 1-5)<br/>requests 250m/256Mi · limits 500m/512Mi<br/>liveness/readiness: /actuator/health/*"]:::deploy
        SvcApp["Service: workshop-app<br/>NodePort · 8080 → 30080"]:::svc
        HPA["HPA: workshop-app-hpa<br/>CPU 70% / Memória 70%<br/>min=1, max=5"]:::deploy

        DeployMysql["Deployment: mysql<br/>1 réplica · strategy Recreate<br/>image mysql:8.0"]:::deploy
        SvcMysql["Service: mysql<br/>ClusterIP · 3306"]:::svc
        PVC["PVC: mysql-pvc<br/>/var/lib/mysql"]:::store

        MetricsServer["metrics-server<br/>vendorizado, --kubelet-insecure-tls<br/>alimenta métricas de CPU/Mem para o HPA"]:::deploy
      end
    end

    Host["Acesso externo<br/>localhost:30080"]:::node

    DockerHub -- "imagePullSecret: dockerhub-secret" --> DeployApp
    CM1 --> DeployApp
    SEC1 --> DeployApp
    CM2 --> DeployMysql
    SEC2 --> DeployMysql
    DeployApp --> SvcApp
    SvcApp -- "NodePort 30080" --> Host
    HPA -. "escala" .-> DeployApp
    MetricsServer -. "métricas" .-> HPA
    DeployApp -- "JDBC :3306" --> SvcMysql
    SvcMysql --> DeployMysql
    DeployMysql --> PVC
```

Manifestos em [`k8s/`](../k8s/), organizados em estágios ordenados de aplicação: `00-namespace` → `01-config` → `02-mysql` → `03-app`.

## Provisionamento via Terraform

Todo o cluster é provisionado declarativamente a partir de [`infra/`](../infra/) — nenhum `kubectl apply` manual:

- **`main.tf`**: providers `tehcyx/kind` (cria o cluster) e o provider oficial `hashicorp/kubernetes` (aplica os recursos).
- **`cluster.tf`**: `kind_cluster.workshop`, com `extra_port_mappings` expondo a porta `30080` do host.
- **`variables.tf`**: 6 variáveis sensíveis, sem valor default — `mysql_root_password`, `mysql_password`, `jwt_secret`, `webhook_token`, `dockerhub_username`, `dockerhub_password`.
- **`secrets.tf`**: cria os 3 Secrets (`app-secret`, `mysql-secret`, `dockerhub-secret`) como recursos nativos `kubernetes_secret`, direto a partir das variáveis acima — nenhum valor real chega a ser escrito em disco ou commitado.
- **`manifests.tf`**: aplica **todos** os arquivos de `/k8s` (namespace, config, MySQL e app) via `kubernetes_manifest` (recurso genérico do provider oficial) lendo os YAML reais com `yamldecode(file(...))`, em vez de reescrever cada recurso como HCL nativo — evita duplicar a mesma definição em dois formatos diferentes. A ordem de aplicação (namespace → config → mysql → app) é garantida por `depends_on` encadeado entre os 4 estágios.
- **`metrics-server.tf`**: mesmo padrão genérico, aplicando os manifestos vendorizados do metrics-server (com o patch `--kubelet-insecure-tls`, necessário porque o kind não tem certificados TLS válidos entre os nós).

### Bootstrap em duas passadas

O recurso `kubernetes_manifest` precisa consultar o schema OpenAPI do cluster já no momento do `terraform plan` — o que cria uma dependência circular na primeira execução, quando o cluster ainda não existe. Por isso, a criação exige duas passadas:

```bash
cd infra
terraform init

# 1ª passada: só o cluster (resolve a dependência circular)
terraform apply -target=kind_cluster.workshop

# 2ª passada: todo o resto (namespace, secrets, config, mysql, app, metrics-server)
terraform apply
```

## Pipeline de CI/CD

GitHub Actions ([`.github/workflows/`](../.github/workflows/)): `ci-cd.yml` orquestra o pipeline e `deploy.yml` é um workflow reutilizável (`workflow_call`) compartilhado pelos dois ambientes simulados — **Homologação** (branch `develop`) e **Produção** (branch `main`).

```mermaid
flowchart TD
    classDef trigger fill:#08427b,stroke:#052e56,color:#fff
    classDef job fill:#438dd5,stroke:#2e6295,color:#fff
    classDef gate fill:#c6e2ff,stroke:#5d82a8,color:#000
    classDef infra fill:#ffd580,stroke:#b38600,color:#000
    classDef ext fill:#999999,stroke:#6b6b6b,color:#fff

    Push["push / PR em develop ou main"]:::trigger

    Push --> BuildTest["build-and-test<br/>mvn -B verify<br/>gate: cobertura JaCoCo ≥ 80%<br/>upload jacoco-report"]:::job

    BuildTest -->|"push (não PR) em develop/main"| BuildPush["build-and-push-image<br/>docker/build-push-action<br/>push para Docker Hub: latest + sha"]:::job

    BuildPush -->|"ref == develop"| DeployHml["deploy-hml (workflow_call)<br/>Environment: homologacao"]:::gate
    BuildPush -->|"ref == main"| DeployProd["deploy-prod (workflow_call)<br/>Environment: producao"]:::gate

    subgraph Deploy["deploy.yml (reusável — roda para hml OU prod)"]
      direction TB
      TfInit["terraform init"]:::infra
      TfBootstrap["terraform apply -target=kind_cluster.workshop<br/>(bootstrap: cluster precisa existir antes<br/>do schema OpenAPI ser lido no plan)"]:::infra
      TfApply["terraform apply<br/>stack completo: namespace, config,<br/>secrets, mysql, app, metrics-server"]:::infra
      Rollout["kubectl rollout status<br/>deployment/workshop-app"]:::infra
      Verify["get pods / get hpa / top nodes"]:::infra
      Smoke["curl localhost:30080/actuator/health<br/>retry até status UP"]:::infra
      TfDestroy["terraform destroy<br/>always(), continue-on-error"]:::infra

      TfInit --> TfBootstrap --> TfApply --> Rollout --> Verify --> Smoke --> TfDestroy
    end

    DeployHml --> Deploy
    DeployProd --> Deploy

    DockerHubExt["Docker Hub"]:::ext
    BuildPush -. "push image" .-> DockerHubExt
    TfApply -. "pull image (dockerhub-secret)" .-> DockerHubExt
```

**Pontos importantes do desenho:**
- `build-and-test` roda em todo push e PR para `develop`/`main` — feedback rápido, sem custo de imagem/deploy.
- `build-and-push-image` e os dois `deploy-*` só rodam em **push** (nunca em PR) para `develop`/`main` — evita builds/deploys desnecessários durante revisão de código.
- O cluster kind roda **dentro do próprio runner** do GitHub Actions (o provider `tehcyx/kind` usa a lib Go `sigs.k8s.io/kind` diretamente, sem depender de nenhum binário externo — só precisa do Docker, que já vem pronto no runner `ubuntu-latest`) — é efêmero, existe só durante o job, e é destruído ao final (`terraform destroy`). O objetivo é provar que a automação de ponta a ponta funciona, não manter um ambiente ativo.
- `homologacao` e `producao` usam exatamente a mesma imagem e o mesmo código Terraform — a diferença entre os dois ambientes está só em qual branch dispara qual GitHub Environment.

## Rodando localmente

Mesmo roteiro usado para validar a infraestrutura manualmente:

```bash
cd infra
terraform init
terraform apply -target=kind_cluster.workshop
terraform apply

kubectl get pods -n workshop
kubectl get hpa -n workshop
curl http://localhost:30080/actuator/health

terraform destroy
```

## Segurança e Secrets

- As 6 variáveis sensíveis (`mysql_root_password`, `mysql_password`, `jwt_secret`, `webhook_token`, `dockerhub_username`, `dockerhub_password`) não têm valor default no `variables.tf` — precisam ser fornecidas via `TF_VAR_<nome>` no ambiente, tanto localmente quanto no pipeline (onde vêm dos GitHub Actions Secrets do repositório).
- `k8s/secret.yaml.example` e `k8s/mysql-secret.yaml.example` são só templates de referência (valores `CHANGE_ME`) — documentam a estrutura esperada caso alguém precise criar um Secret manualmente via `kubectl`, mas não são lidos pelo Terraform nem aplicados automaticamente.
- `infra/.terraform/` e `infra/terraform.tfstate*` estão no `.gitignore` — o state do Terraform armazena os valores dos secrets em texto plano, então nunca é commitado.
