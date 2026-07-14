# Arquitetura

Documentação de arquitetura do Workshop Management System, usando o modelo **C4** (Contexto → Container → Componente) para descrever o sistema em níveis crescentes de detalhe. Para o modelo de dados (diagrama entidade-relacionamento e justificativa de escolha do MySQL), ver a seção [Modelagem do Banco de Dados](../README.md#modelagem-do-banco-de-dados) no README.

## Nível 1 — Contexto

Quem interage com o sistema e por quê:

```mermaid
flowchart TB
    classDef person fill:#08427b,stroke:#052e56,color:#fff
    classDef system fill:#1168bd,stroke:#0b4884,color:#fff
    classDef external fill:#999999,stroke:#6b6b6b,color:#fff

    Cliente["Cliente<br/>[Pessoa]<br/>Consulta o status da sua OS<br/>via código público, sem login"]:::person
    Funcionario["Funcionário da Oficina<br/>[Pessoa]<br/>Consultor, Mecânico, Estoquista ou Admin<br/>autenticado via JWT"]:::person
    ExternoWebhook["Sistema Externo de Notificação<br/>[Sistema Externo]<br/>Confirma aprovação de orçamento e<br/>atualização de status por e-mail"]:::external

    Workshop["Workshop Management System<br/>[Sistema]<br/>Gerencia clientes, veículos, ordens de<br/>serviço, catálogo e estoque de peças<br/>de uma oficina mecânica"]:::system

    Cliente -- "Consulta status da OS<br/>GET /tracking/{osCode} — JSON/HTTPS" --> Workshop
    Funcionario -- "Login, cadastros, gestão de OS e<br/>estoque — JSON/HTTPS + JWT" --> Workshop
    ExternoWebhook -- "POST /webhooks/estimate-approval<br/>POST /webhooks/email-status-update<br/>JSON/HTTPS + X-Webhook-Token" --> Workshop
```

## Nível 2 — Containers

```mermaid
flowchart TB
    classDef person fill:#08427b,stroke:#052e56,color:#fff
    classDef container fill:#438dd5,stroke:#2e6295,color:#fff
    classDef external fill:#999999,stroke:#6b6b6b,color:#fff

    Cliente["Cliente<br/>[Pessoa]"]:::person
    Funcionario["Funcionário da Oficina<br/>[Pessoa]"]:::person
    ExternoWebhook["Sistema Externo de Notificação<br/>[Sistema Externo]"]:::external

    subgraph Workshop["Workshop Management System"]
        direction TB
        API["Workshop API<br/>[Container: Spring Boot 3.2 / Java 17]<br/>REST API (interfaces/rest), autenticação<br/>JWT, regras de negócio, expõe<br/>Swagger UI e /v3/api-docs"]:::container
        DB[("Banco de Dados<br/>[Container: MySQL 8]<br/>Clientes, veículos, OS, catálogo,<br/>peças e usuários — schema<br/>versionado via Flyway")]:::container
    end

    Cliente -- "GET /api/v1/tracking/{osCode}<br/>JSON/HTTPS" --> API
    Funcionario -- "REST + JWT Bearer<br/>JSON/HTTPS · porta 8080" --> API
    ExternoWebhook -- "POST /api/v1/webhooks/*<br/>JSON/HTTPS + X-Webhook-Token" --> API
    API -- "JDBC (Spring Data JPA / Hibernate)<br/>porta 3306" --> DB
```

**Monólito por decisão de escopo, não microsserviços**: um único container Spring Boot deployável. Swagger UI e o endpoint `/v3/api-docs` são rotas dentro desse mesmo container, não um serviço separado.

## Nível 3 — Componentes (dentro da Workshop API)

Mapeamento direto dos pacotes reais do projeto (contagem de classes atual):

```mermaid
flowchart TB
    classDef controller fill:#85bbf0,stroke:#5d82a8,color:#000
    classDef port fill:#c6e2ff,stroke:#5d82a8,color:#000
    classDef domain fill:#ffd580,stroke:#b38600,color:#000
    classDef infra fill:#b8e0b8,stroke:#4a8f4a,color:#000
    classDef db fill:#438dd5,stroke:#2e6295,color:#fff

    subgraph REST["interfaces/rest — 9 classes"]
        Controllers["AuthController · CustomerController · VehicleController<br/>ServiceCatalogController · SupplyController · ServiceOrderController<br/>TrackingController · WebhookController<br/>+ GlobalExceptionHandler"]:::controller
    end

    subgraph APP["application"]
        direction TB
        PortsIn["port/in — 33 interfaces<br/>catalog · customer · serviceorder · supply · vehicle"]:::port
        UseCases["usecase — 33 classes<br/>implementam os 33 input ports<br/>(1 use case por operação de negócio)"]:::port
        DTO["dto — 26 classes<br/>Commands e Responses (records)"]:::port
    end

    subgraph DOM["domain"]
        direction TB
        Model["model — 12 classes<br/>catalog · customer · serviceorder · shared · supply · vehicle"]:::domain
        Repo["repository — 5 interfaces<br/>(convenção do projeto: 'Repository', não 'Gateway')"]:::domain
        DomSvc["service — 5 interfaces<br/>ex.: notificação de estimate, alerta de estoque"]:::domain
        DomExc["exception — 7 classes"]:::domain
    end

    subgraph INFRA["infrastructure"]
        direction TB
        Persist["persistence — 22 classes<br/>adapter · entity · mapper · repository (Spring Data)"]:::infra
        Security["security — 6 classes<br/>JWT filter/service, UserDetails, SecurityConfig"]:::infra
        Config["config — 2 classes<br/>OpenApiConfig, DomainServiceConfig"]:::infra
        Notif["notification — 2 classes<br/>Mock*NotificationService"]:::infra
    end

    MySQL[("MySQL 8")]:::db

    Controllers -- "chama" --> PortsIn
    PortsIn -. "implementado por" .-> UseCases
    UseCases -- "usa" --> DTO
    UseCases -- "orquestra" --> Model
    UseCases -- "usa" --> Repo
    UseCases -- "usa" --> DomSvc
    UseCases -- "lança" --> DomExc
    Repo -. "implementado por" .-> Persist
    DomSvc -. "implementado por" .-> Notif
    Persist -- "mapeia para/de" --> Model
    Persist -- "JDBC" --> MySQL
    Security -- "intercepta requisições" --> Controllers
```

As setas tracejadas ("implementado por") são o ponto central da Clean Architecture aplicada aqui: `domain/repository` e `domain/service` são abstrações; `infrastructure/persistence/adapter` e `infrastructure/notification` são as implementações, apontando **de volta** para dentro — a camada de infraestrutura depende do domínio, nunca o contrário (inversão de dependência).

## Decisões Técnicas

- **Clean Architecture com inversão de dependência**: `interfaces/rest` conhece só as interfaces de `application/port/in`; os casos de uso (`application/usecase`) implementam essas interfaces e dependem só de abstrações do domínio (`domain/repository`, `domain/service`); os adapters de infraestrutura implementam essas abstrações. O domínio (`domain/model`) é POJO puro, sem nenhuma dependência de framework — testável sem subir o contexto Spring.
- **`Repository`, não `Gateway`**: as interfaces de acesso a dados em `domain/repository` mantêm o nome `Repository` (não `Gateway`, como em alguns materiais de Clean Architecture) por consistência com a terminologia já consolidada em Spring Data/DDD no restante do código — a equivalência conceitual é a mesma, é só uma escolha de nomenclatura.
- **JWT stateless**: autenticação via `Authorization: Bearer <token>`, sem sessão no servidor — compatível com múltiplas réplicas do container por trás do HPA (ver [DEPLOYMENT.md](DEPLOYMENT.md)) sem necessidade de sticky sessions ou store de sessão compartilhado.
- **Monólito**: no escopo atual (uma API de gestão de oficina, um único time, um domínio coeso), um monólito modular bem estruturado em camadas entrega os mesmos benefícios de manutenibilidade de uma arquitetura distribuída, sem a complexidade operacional adicional (múltiplos deploys, comunicação de rede entre serviços, consistência eventual) que não se paga nesta escala.
