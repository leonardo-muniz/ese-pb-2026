# Exchange API - Migração para Arquitetura de Microsserviços (TP3)

[![Build passing](https://github.com/leonardo-muniz/ese-pb-2026/actions/workflows/deploy.yml/badge.svg?branch=tp4)](https://github.com/leonardo-muniz/ese-pb-2026/actions/workflows/deploy.yml?query=branch%3Atp4)

## Objetivo

O objetivo desta atividade foi migrar a aplicação desenvolvida no TP2, originalmente implementada como um sistema monolítico com persistência em banco de dados, para uma arquitetura baseada em microsserviços.

A proposta teve como foco aplicar os conceitos de desacoplamento, modularização e escalabilidade, permitindo que diferentes partes do sistema possam evoluir de forma independente.

---

### Desenvolvimento

Para atender aos requisitos do TP3, a aplicação foi refatorada, separando as responsabilidades anteriormente concentradas em um único sistema.

As principais alterações realizadas foram:

- Separação da lógica de negócio em microsserviços independentes;
- Exposição das funcionalidades através de APIs REST;
- Manutenção da camada de persistência existente;
- Isolamento das responsabilidades de cada domínio da aplicação;
- Utilização de containers Docker para facilitar a execução do ambiente;
- Organização da arquitetura visando maior escalabilidade e facilidade de manutenção.

A comunicação entre os componentes ocorre por meio de requisições HTTP, permitindo que cada serviço execute suas responsabilidades de forma isolada.

---

## 🛠️ Tecnologias e Versões Utilizadas

### Back-End

- **Linguagem:** Java 25
- **Framework:** Spring Boot 4.1.1
- **Gerenciamento de Dependências:** Maven
- **Bibliotecas Principais:**
  - `Spring Web` (APIs RESTful)
  - `Spring Security` (Autenticação e Proteção de Rotas)
  - `Spring Boot Validation` (Validações de DTOs com Jakarta)
  - `Lombok` (Redução de código boilerplate)
  - `Spring Boot Actuator` (Saúde da API)
  - `Spring Cloud 2025.1.3` (Cloud Infrastructure)
  - `Spring Cloud Gateway` (Roteamento centralizado de requisições)
  - `Eureka Discovery Server` (Descoberta e registro de serviços)
  - `Spring Config Server` (Configuração centralizada dos microsserviços)

### Front-End (Interface do Usuário)

- **Biblioteca Base:** React (v18+) com TypeScript (tipagem estática rigorosa).
- **Build Tool & Bundler:** Vite (configuração rápida e variáveis de ambiente).
- **Roteamento:** `react-router-dom` com implementação de rotas privadas (proteção de acesso ao painel administrativo).
- **Estilização e UI:** Tailwind CSS para estilização utilitária ágil e responsiva, juntamente com `lucide-react` para iconografia moderna.
- **Arquitetura e Boas Práticas:**
  - Componentização: Divisão clara entre `pages`, `components`, `hooks` e `types`.
  - Separação de Responsabilidades: Uso de Custom Hooks (como `useUsers`) para isolar regras de negócio, controle de estado e chamadas à API da camada de visualização.
- **Comunicação e Resiliência (Mock Mode):**
  - Consumo da API REST através de chamadas assíncronas com `fetch`.
  - **Sandbox Local (Fallback):** Mecanismo automático que detecta quando o backend está offline e ativa um modo de simulação em memória. Isso permite testar 100% das operações de CRUD na interface visual sem dependência do servidor ativo.
- **UX (Experiência do Usuário):**
  - Sistema próprio de notificações flutuantes (Toast) com temporizadores e animações.
  - Monitoramento em tempo real do status da API (Ping a cada 30s).
  - *Empty States* dinâmicos que orientam o usuário caso o banco ou simulador estejam vazios.
  - Autenticação simulada baseada em `localStorage`.

### Persistência

- Postgres 18 (configurável via Docker)

> [!TIP]
> Configure outro banco de dados facilmente no docker-compose.yml.

```env
DB_URL: jdbc:postgresql://wallet-db:5433/wallet_db
DB_USER: postgres
DB_PASSWORD: postgres # PLEASE CHANGE !
DB_DRIVER: org.postgresql.Driver
DB_DIALECT: org.hibernate.dialect.PostgreSQLDialect
```

#### Exemplo da configuração para MySQL

```env
DB_URL: jdbc:mysql://wallet-db:3306/wallet_db?serverTimezone=UTC&useSSL=false
DB_USER: root
DB_PASSWORD: mysql # PLEASE CHANGE !
DB_DRIVER: com.mysql.cj.jdbc.Driver
DB_DIALECT: org.hibernate.dialect.MySQLDialect
```

> [!WARNING]
> Não esqueça de alterar o banco de dados no pom.xml da mesma forma.

### Infraestrutura

- Docker
- Docker Compose

---

## 🗺️ Modelagem Estratégica DDD: Domínios, Subdomínios e Bounded Contexts

Para projetar a API da Exchange, foi dividido o problema de negócio utilizando a modelagem estratégica do **Domain-Driven Design (DDD)** que permitiu separar as regras complexas de execução de transações da infraestrutura básica de gerenciamento de contas e saldos.

### 1. Espaço de Problema (Domínios e Subdomínios)

- **Core Domain (Domínio Central):** `Motor de Negociação (Trade / Exchange)`
  - O coração do produto, onde o valor é entregue diretamente ao cliente através da execução de ordens de compra e venda de criptomoedas de forma ágil e segura.

- **Supporting Subdomain (Subdomínio de Suporte):** `Identity & Access (Gestão de Usuários)`
  - Essencial para o funcionamento do sistema financeiro (identificação do cliente), mas não é o diferencial competitivo que gera receita direta para a Exchange.

- **Generic Subdomain (Subdomínio Genérico):** `Gestão de Carteiras (Wallet & Ledger)`
  - Lógica de controle de saldos em moedas fiduciárias (USD) e criptoativos (BTC). Mecanismos de débito/crédito são problemas já resolvidos no mercado e poderiam ser delegados a provedores genéricos (BaaS), mas aqui foram implementados para gerenciar a liquidez das operações localmente.

### 2. Espaço de Solução (Bounded Contexts)

Foi possível identificar três **Bounded Contexts** (Contextos Delimitados) claros no monólito, cada um possuindo sua própria linguagem ubíqua e responsabilidades bem definidas:

| Bounded Context | Domínio/Subdomínio Associado      | Principais Agregados / Entidades | Responsabilidade Principal                                                                                                                            |
|-----------------|-----------------------------------|----------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|
| Trade Context   | Core Domain (Negociação)          | Order (Aggregate Root)           | Controlar a criação de ordens (compra/venda), calcular custos totais baseados no preço de mercado e registrar o status da transação.                  |
| Wallet Context  | Generic Subdomain (Carteiras)     | Wallet (Aggregate Root)          | Gerenciar os saldos individuais de cada ativo (fiduciário ou cripto), processando depósitos, saques e verificando fundos antes de aprovar transações. |
| User Context    | Supporting Subdomain (Identidade) | User (Aggregate Root)            | Manter os dados cadastrais básicos necessários para vincular as carteiras financeiras e o histórico de ordens ao proprietário correto.                |

### Arquitetura e Padrões

- **Design de Software:** Evolução do padrão **Monólito Modular** para uma arquitetura baseada em **Microsserviços**, mantendo a separação de responsabilidades através das camadas Controller, Service e Repository em cada serviço. Essa abordagem promove baixo acoplamento, alta coesão e independência de implantação dos componentes da aplicação.

- **Domain-Driven Design (DDD):** Os *Bounded Contexts* (`User`, `Wallet`, `Trade`) anteriormente organizados de forma lógica dentro do monólito foram transformados em serviços independentes, preservando os limites do domínio de negócio e fortalecendo o isolamento funcional entre os contextos.

- **Estratégia de Persistência (Escopo TP3):** A persistência foi descentralizada seguindo o princípio **Database per Service**, permitindo que cada microsserviço seja responsável pelos seus próprios dados. Foram mantidas as integrações com **Spring Data JPA**, **Hibernate** e bancos relacionais, garantindo independência dos repositórios e redução do acoplamento entre domínios.

- **Comunicação entre Serviços:** Os endpoints REST continuam sendo usados para consultas e comandos síncronos de baixa latência. Para o fluxo de negociação, a integração entre `Trade` e `Wallet` foi refatorada para eventos assíncronos com RabbitMQ, evitando que a criação de uma ordem dependa da disponibilidade imediata do outro serviço.

## Arquitetura Orientada a Eventos

### Avaliação

Arquitetura orientada a eventos (EDA) organiza a comunicação por fatos ou comandos publicados em um broker. O produtor não precisa conhecer a implementação ou a disponibilidade dos consumidores.

**Vantagens:**

- reduz o acoplamento temporal e de implementação entre microsserviços;
- permite absorver picos de tráfego com filas e consumidores escaláveis;
- oferece retry, confirmação, roteamento e armazenamento temporário de mensagens;
- facilita a inclusão de novos consumidores, como auditoria, notificações e analytics, sem alterar o produtor.

**Desvantagens:**

- a consistência entre bancos passa a ser eventual, exigindo estados intermediários e observabilidade;
- rastrear uma operação distribuída é mais difícil do que rastrear uma chamada HTTP;
- mensagens podem ser duplicadas, fora de ordem ou rejeitadas, portanto consumidores precisam ser idempotentes;
- o broker se torna uma dependência operacional adicional e contratos de mensagens precisam de versionamento.

EDA é mais vantajosa quando a operação admite processamento assíncrono, há picos de carga ou vários consumidores precisam reagir ao mesmo fato. Não é a melhor escolha para validações que exigem resposta imediata e consistência forte entre múltiplos dados.

### Padrões de mensagens usados

| Padrão | Aplicação no sistema | Decisão |
|---|---|---|
| **Command** | `wallet.debit.requested` solicita que o Wallet debite uma carteira | O Trade envia somente o contrato necessário, sem compartilhar entidades ou banco |
| **Event notification** | Uma ordem criada pode futuramente notificar auditoria ou notificações | Novos consumidores podem ser ligados ao mesmo topic exchange |
| **Publish/subscribe** | O exchange `exchange.events` roteia mensagens por routing key | Produtores não conhecem a quantidade de consumidores |
| **Dead letter queue** | `wallet.debit.requested.dlq` recebe falhas não reencaminhadas | Mensagens inválidas ficam disponíveis para inspeção e reprocessamento |

### Implementação RabbitMQ e Spring Boot

O `Trade Service` persiste a ordem como `OPEN` e publica um `WalletDebitRequested` no topic exchange `exchange.events`. O `Wallet Service` consome a fila durável `wallet.debit.requested.queue`, executa o débito em uma transação local e registra o `eventId` em `processed_events`. Assim, uma reentrega do RabbitMQ não gera um segundo débito. Falhas são encaminhadas para a dead-letter queue.

```mermaid
flowchart LR
  API[POST /orders] --> TRADE[Trade Service]
  TRADE --> TDB[(Trade DB)]
  TRADE -->|wallet.debit.requested| EX((exchange.events))
  EX --> Q[wallet.debit.requested.queue]
  Q --> WALLET[Wallet Service]
  WALLET --> WDB[(Wallet DB)]
  Q -. falha .-> DLX((exchange.events.dlx))
  DLX --> DLQ[wallet.debit.requested.dlq]
```

O Spring Boot simplifica a integração por meio de `RabbitTemplate`, `@RabbitListener`, `JacksonJsonMessageConverter` e beans declarativos de `TopicExchange`, `Queue` e `Binding`. A configuração usa variáveis de ambiente (`RABBITMQ_HOST`, `RABBITMQ_USER` e `RABBITMQ_PASSWORD`) e o `docker-compose` inclui o RabbitMQ com painel de gerenciamento na porta `15672`.

### Refatoração e limites

Antes, o `Trade Service` consultava carteiras e executava `withdraw` por Feign durante a requisição HTTP. Agora ele publica um comando e o `Wallet Service` é o único responsável pelo saldo. Isso melhora a disponibilidade e a escalabilidade, mas a resposta da criação da ordem representa o aceite do pedido, não a conclusão do débito. Em uma evolução de produção, o próximo passo é usar o padrão **Transactional Outbox** no Trade para garantir a publicação do evento junto com a persistência da ordem, além de publicar eventos de sucesso ou falha para atualizar o status da ordem.

- **Observabilidade e Resiliência:** Estrutura preparada para monitoramento individual dos serviços, facilitando identificação de falhas, rastreamento de requisições e manutenção independente dos componentes distribuídos.

- **Design de API:** Cada microsserviço expõe uma API RESTful *Stateless*, mantendo a padronização de respostas, contratos bem definidos e utilização adequada dos códigos de status HTTP para representar o resultado das operações.

- **Tratamento de Exceções:** Utilização de interceptadores globais de exceção em cada serviço, garantindo tratamento consistente para regras de negócio, validações e recursos inexistentes, mantendo uniformidade nas respostas de erro da arquitetura distribuída.

- **Padrões de Projeto Aplicados:** Uso de *DTO (Data Transfer Object)* para isolamento dos contratos de comunicação, *Builder Pattern* para criação consistente das entidades de domínio e *Repository Pattern* através do Spring Data JPA para abstração da camada de persistência.

- **Containerização e Implantação:** Utilização do **Docker** para empacotamento dos microsserviços e padronização do ambiente de execução, simplificando o processo de implantação, testes e escalabilidade dos componentes da solução.

---

## 💾 Modelagem de Dados e Arquitetura (Microsserviços & Clean Code)

A arquitetura do projeto foi evoluída seguindo os princípios de **Clean Code**, **Domain-Driven Design (DDD)** e arquitetura de **Microsserviços**. Os contextos de negócio, anteriormente organizados dentro de um Monólito Modular, foram desacoplados em serviços independentes, cada um responsável por seu domínio, regras de negócio e camada de persistência.

A modelagem foi estruturada de forma a preservar a separação clara entre domínio, aplicação e infraestrutura, mantendo as regras de negócio isoladas dos detalhes tecnológicos. Cada microsserviço possui autonomia sobre seus dados, adotando o princípio de **Database per Service**, reduzindo o acoplamento entre módulos e favorecendo a evolução independente da solução.

Essa abordagem garante maior encapsulamento, facilita a manutenção e escalabilidade da aplicação, além de permitir que os serviços sejam desenvolvidos, implantados e versionados de forma independente, sem comprometer a consistência das regras centrais do domínio.

---

## 📊 Arquitetura do Sistema

Abaixo estão os diagramas que ilustram a separação de responsabilidades e o fluxo de dados da nossa API.

### 1. Diagrama de Componentes

Este diagrama demonstra a composição da arquitetura distribuída, incluindo Service Discovery, Config Server, API Gateway e os microsserviços responsáveis pelos domínios de negócio.

```mermaid
graph TD
    classDef client fill:#f9f,stroke:#333,stroke-width:2px,color:#000
    classDef infra fill:#ffd580,stroke:#333,stroke-width:2px,color:#000
    classDef service fill:#bfb,stroke:#333,stroke-width:2px,color:#000
    classDef database fill:#ddd,stroke:#333,stroke-width:2px,color:#000

    Client["Front-end React / Postman"]:::client

    Gateway["API Gateway"]:::infra
    Eureka["Eureka Server"]:::infra
    Config["Config Server"]:::infra
    Repo["Config Repository"]:::infra

    UserService["User Service"]:::service
    WalletService["Wallet Service"]:::service
    TradeService["Trade Service"]:::service

    UserDB["User Database"]:::database
    WalletDB["Wallet Database + processed_events"]:::database
    TradeDB["Trade Database"]:::database

    Rabbit["RabbitMQ"]:::infra
    Events["exchange.events"]:::infra
    DebitQueue["wallet.debit.requested.queue"]:::service
    DeadLetter["exchange.events.dlx"]:::infra
    DeadQueue["wallet.debit.requested.dlq"]:::service

    Client -->|HTTP REST| Gateway

    Gateway --> UserService
    Gateway --> WalletService
    Gateway --> TradeService

    Config --> Repo

    UserService -->|Configuração| Config
    WalletService -->|Configuração| Config
    TradeService -->|Configuração| Config
    Gateway -->|Configuração| Config

    UserService -->|Registro| Eureka
    WalletService -->|Registro| Eureka
    TradeService -->|Registro| Eureka
    Gateway -->|Discovery| Eureka

    UserService --> UserDB
    WalletService --> WalletDB
    TradeService --> TradeDB

    TradeService -->|wallet.debit.requested| Rabbit
    Rabbit --> Events
    Events --> DebitQueue
    DebitQueue -->|Rabbit listener| WalletService
    DebitQueue -.->|rejeição| DeadLetter
    DeadLetter --> DeadQueue
```

### 2. Diagrama de Sequência (Caso de Uso: Nova Ordem de Compra)

O fluxo abaixo ilustra a comunicação assíncrona entre Gateway, Trade Service, RabbitMQ e Wallet Service durante a criação de uma nova ordem.

```mermaid
sequenceDiagram
    autonumber

    actor Client as Front-End React
    participant GW as API Gateway
    participant TS as Trade Service
    participant TDB as Trade Database
    participant RM as RabbitMQ
    participant Q as Wallet Debit Queue
    participant WS as Wallet Service
    participant WDB as Wallet Database
    participant DLQ as Dead Letter Queue

    Client->>GW: POST /orders
    activate GW
    GW->>TS: Encaminha requisição
    activate TS

    TS->>TDB: INSERT Order (OPEN)
    TDB-->>TS: Order persisted
    TS->>RM: Publish wallet.debit.requested
    RM->>Q: Route by routing key

    TS-->>GW: 201 Created (order OPEN)
    deactivate TS
    GW-->>Client: Order accepted for processing
    deactivate GW

    Q->>WS: Deliver WalletDebitRequested
    activate WS

    alt Débito processado
        WS->>WDB: Find wallet and debit USD
        WS->>WDB: Save eventId in processed_events
        WDB-->>WS: Transaction committed
        WS-->>Q: ACK
    else Falha no processamento
        WS-->>Q: Reject message
        Q->>DLQ: Route to exchange.events.dlx
        Note over DLQ: Aguarda inspeção ou reprocessamento
    end

    deactivate WS
```

## 🐳 Infraestrutura e Execução (Docker)

Para garantir um ambiente de desenvolvimento padronizado e facilitar a execução do banco de dados relacional, o projeto utiliza o Docker. A infraestrutura do PostgreSQL, Node.js (Front-end) e API estão configuradas no arquivo `docker-compose.yml`.

### **Pré-requisitos:**

- **Docker** e **Docker Compose** instalados na máquina.
- **Java 25** e **Maven** (para os testes locais).

#### Passo a passo para execução usando Docker

1. Na raiz do projeto, suba os containeres do banco de dados, da API e do front-end em segundo plano:

   ```bash
   cd infra
   docker compose up --build -d
   ```

    Ao iniciar, o Hibernate criará e atualizará as tabelas automaticamente (`ddl-auto=update`).

2. Você pode acessar o front-end através da porta 5173 (`localhost:5173`) e a API pela porta 8080 (`localhost:8080`).

#### Passos para execução local e de testes

1. Para rodar a API localmente, execute a classe principal da aplicação ou utilize o comando Maven:

    ```bash
    cd <projeto>
    mvn spring-boot:run
    # Windows
    mvnw spring-boot:run
    ```

2. Para executar a suíte de testes (que utiliza o banco H2 em memória e não interfere no banco de dados principal), rode:

    ```bash
    mvn clean test
    # Windows
    mvnw clean test
    ```

> [!TIP]
> Execução com as imagens do GitHub Container Registry (GHCR)

```bash
cd infra
docker compose -f docker-compose.prod.yml up --build -d
```

---

### Benefícios Obtidos

A adoção de uma arquitetura baseada em microsserviços proporcionou diversos benefícios para a aplicação:

- Redução do acoplamento entre módulos;
- Melhor organização do código-fonte;
- Maior facilidade de manutenção;
- Possibilidade de evolução independente dos serviços;
- Melhor preparação da aplicação para cenários de escalabilidade;
- Implantação mais flexível dos componentes.

---

### Resultados

A aplicação manteve todas as funcionalidades implementadas no TP2, porém agora distribuídas entre microsserviços independentes.

Os testes realizados demonstraram que:

- Os serviços conseguem se comunicar corretamente;
- A persistência dos dados continua funcionando normalmente;
- Os componentes podem ser executados de forma independente;
- O ambiente pode ser reproduzido utilizando Docker.

---

### Conclusão

A migração do monólito para microsserviços permitiu aplicar conceitos fundamentais de Engenharia de Software Escalável, tornando a aplicação mais modular, flexível e preparada para crescimento futuro. A nova arquitetura facilita a manutenção do sistema e possibilita a evolução dos componentes de forma independente.
