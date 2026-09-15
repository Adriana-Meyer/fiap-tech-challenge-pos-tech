# ADR 0010: Dois caminhos de deploy no Repositório 4, aditivos

**Status**: Aceito
**Data**: 2026-09-13

## Contexto

Antes da Fase 3, o pipeline de CI/CD (`ci-cd.yml` + `deploy.yml`) já sobe e derruba um cluster **kind** efêmero dentro do próprio runner do GitHub Actions, a cada push, para validar o deploy de ponta a ponta sem custo de nuvem. A Fase 3 pede deploy real contra o EKS ([Repositório 2](https://github.com/Adriana-Meyer/fiap-tech-challenge-kubernetes-infrastructure)) e o RDS ([Repositório 3](https://github.com/Adriana-Meyer/fiap-tech-challenge-database-infrastructure)).

O cluster EKS real, porém, não fica sempre no ar — é provisionado e destruído por sessão de estudo do AWS Academy Learner Lab, para preservar orçamento. Substituir o pipeline kind pelo deploy real faria todo push no `develop` falhar sempre que o cluster estivesse desligado (a maior parte do tempo durante o desenvolvimento).

## Decisão

Os dois caminhos coexistem, aditivos, sem que um substitua o outro por enquanto:
- **kind** (`ci-cd.yml`/`deploy.yml`, automático a cada push): validação gratuita e rápida, independente de qualquer sessão AWS. Usa `k8s/00-namespace`, `01-config` (incluindo `mysql-configmap.yaml`), `02-mysql`, `03-app/service.yaml` (NodePort).
- **AWS real** (`deploy-aws.yml`, `workflow_dispatch` manual): `aws eks update-kubeconfig` + `kubectl apply` seletivo contra o cluster do [Repositório 2](https://github.com/Adriana-Meyer/fiap-tech-challenge-kubernetes-infrastructure) e o RDS do [Repositório 3](https://github.com/Adriana-Meyer/fiap-tech-challenge-database-infrastructure). Usa `k8s/00-namespace`, `01-config/app-configmap.yaml` (sem o `mysql-configmap.yaml`), `03-app/deployment.yaml`+`hpa.yaml`, e `k8s/aws/service-loadbalancer.yaml` (LoadBalancer) — pasta separada de `03-app/` de propósito, porque `infra/manifests.tf` (usado pelo caminho kind) aplica genericamente todo `.yaml` das 4 pastas fixas (`00-namespace`, `01-config`, `02-mysql`, `03-app`); um segundo Service `workshop-app` dentro de `03-app/` colidia com o `service.yaml` do kind.

A pasta `infra/` (Terraform do kind) e `k8s/02-mysql/*` são mantidas por ora — serão removidas quando o deploy real na AWS for testado de ponta a ponta, ponto em que `deploy-aws.yml` passa a ser o deploy automático de produção, substituindo o kind.

## Consequências

- Nenhuma mudança nos arquivos que o kind já usava foi necessária para adicionar o caminho AWS — reduz risco de regressão no pipeline já validado.
- Existe duplicação temporária entre `k8s/03-app/service.yaml` (NodePort, kind) e `k8s/aws/service-loadbalancer.yaml` (LoadBalancer, AWS) — aceita como transitória, até a remoção do caminho kind.
- Documentação (READMEs, `docs/DEPLOYMENT.md`) precisa deixar claro qual caminho cada arquivo pertence, para não confundir alguém lendo o repositório pela primeira vez.
