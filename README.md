# Oficina Mecânica - SOAT Tech Challenge

[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=mateuslourencao_soat-fase1-tech-challenge&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mateuslourencao_soat-fase1-tech-challenge)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=mateuslourencao_soat-fase1-tech-challenge&metric=security_rating)](https://sonarcloud.io/summary/overall?id=mateuslourencao_soat-fase1-tech-challenge&branch=main)

## Sobre o Projeto

Este projeto é um MVP de um Sistema de Oficina Mecânica, desenvolvido como parte do Tech Challenge da Pós-Graduação em Arquitetura de Software (SOAT). A aplicação gerencia o fluxo administrativo, de estoque e de manutenção de uma oficina.

## Tecnologias Utilizadas

- **Java 21** - Linguagem principal
- **Spring Boot 4.1.0** - Framework para construção da aplicação
- **Spring Data JPA** - Abstração de persistência de dados
- **Spring Security & JWT** - Autenticação e autorização
- **MySQL 8.0** - Banco de dados relacional
- **Maven** - Gerenciador de dependências e build
- **Docker & Docker Compose** - Conteinerização da aplicação e banco de dados
- **SpringDoc OpenAPI** - Documentação da API
- **JaCoCo** - Relatórios de cobertura de testes
- **SonarQube** - Análise de qualidade e segurança

## Arquitetura

O projeto segue os princípios da **Arquitetura Hexagonal (Ports and Adapters)**, organizado em módulos de domínio para garantir separação de responsabilidades e facilitar a testabilidade.

### Módulos Principais

- **Administrativo** - Gestão de funcionários e autenticação
- **Estoque** - Controle de peças e serviços
- **Manutenção** - Gestão de clientes, veículos e ordens de serviço

## Configurações iniciais

### Kubernetes local provisionado com Terraform

O Terraform provisiona um cluster Kubernetes local chamado `oficina-local` usando [Kind](https://kind.sigs.k8s.io/). O Kind executa os nós Kubernetes como contêineres Docker; neste projeto, o Docker Desktop fornece o runtime. Portanto, **o contexto do cluster usado pelo projeto é `kind-oficina-local`**, e não `docker-desktop` (que identifica o cluster Kubernetes gerenciado diretamente pelo Docker Desktop).

Além do cluster, o Terraform constrói a imagem da API, carrega essa imagem no cluster Kind e aplica o `kustomization.yaml` da raiz. Os manifests criam:

- API da oficina, com três réplicas e Service NodePort;
- MySQL, com Service interno e volume persistente de 2 GiB;
- MailHog, com interface web e SMTP;
- ConfigMap do banco de dados e das configurações da aplicação, Secret de desenvolvimento e ConfigMap com o `schema.sql`.

#### Pré-requisitos

- Windows com Docker Desktop iniciado e configurado para **Linux containers**;
- Terraform `>= 1.5.0`;
- `kubectl`;
- Kind instalado e disponível no `PATH`. No Windows, pode ser instalado com [WinGet](https://kind.sigs.k8s.io/docs/user/quick-start/):

```powershell
winget install Kubernetes.kind
```

Após instalar, abra um novo PowerShell e confirme as ferramentas e o contexto Docker:

```powershell
docker context show
kind version
kubectl version --client
terraform version
```

O contexto Docker deve apontar para o Docker Desktop (normalmente `desktop-linux`). Se necessário, selecione-o com `docker context use desktop-linux`.

#### Provisionar e iniciar

Na raiz do repositório, inicialize o Terraform e aplique a configuração. O `apply` apresenta o plano e pede confirmação antes de criar os recursos:

```powershell
terraform -chdir=infra/kind init
terraform -chdir=infra/kind plan
terraform -chdir=infra/kind apply
```

Durante a aplicação, o Terraform cria o cluster, constrói a imagem `soat-fase1-tech-challenge-app:dev`, carrega-a no Kind e aplica os manifests Kubernetes. O provisionamento aguarda os deployments do banco, da API e do MailHog ficarem prontos.

#### Conferir o ambiente

```powershell
kubectl --context kind-oficina-local get nodes
kubectl --context kind-oficina-local get deployments,services,pods
```

- API e Swagger: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- Interface do MailHog: [http://localhost:8025](http://localhost:8025)

O MySQL fica acessível dentro do cluster pelo Service `oficina-db-svc:3306`. A senha e o segredo JWT presentes nos manifests são valores de desenvolvimento; não os reutilize em produção.

#### Remover o ambiente

Para destruir os recursos gerenciados pelo Terraform, incluindo o cluster Kind:

```powershell
terraform -chdir=infra/kind destroy
```

Ao destruir o cluster, os recursos e os dados persistidos no volume local do MySQL também são removidos.

### Pré-requisitos

- Docker e Docker Compose instalados
- Java 21 e Maven (opcional, para execução local)

### Instalação com Docker (Recomendado)

Na raiz do projeto, execute:

```bash
docker-compose up --build
```

Isso iniciará:
1. Container MySQL (`oficina-mysql`) na porta `3306` (inicializado com `schema.sql`)
2. Container MailHog (`oficina-mailhog`) com SMTP na porta `1025` e interface web na porta `8025`
3. Container da aplicação (`oficina-app`) na porta `8081`

A aplicação aguardará o banco de dados estar pronto antes de iniciar.

Os e-mails enviados pela aplicação ficam disponíveis para consulta em:

**[http://localhost:8025](http://localhost:8025)**

### Instalação Local

1. Certifique-se de ter um banco MySQL rodando
2. Configure as variáveis de ambiente ou edite `src/main/resources/application.properties`:
   - `DB_URL` - URL de conexão (ex: `jdbc:mysql://localhost:3306/oficina`)
   - `DB_USERNAME` - Usuário do banco
   - `DB_PASSWORD` - Senha do banco

3. Execute via Maven:

```bash
mvn clean spring-boot:run
```

### Banco de Dados

O Hibernate está configurado para criar tabelas automaticamente (`ddl-auto: update`). Um script SQL completo está disponível para referência:

- **Arquivo**: `schema.sql` (na raiz do projeto)
- **Conteúdo**: Definição de tabelas, índices e restrições

## Documentação da API

Com a aplicação rodando, acesse a documentação interativa do Swagger:

**[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)**

### Principais Endpoints

- `/api/v1/administrativo/autenticacao` - Login de funcionários
- `/api/v1/administrativo/funcionarios` - Gestão de funcionários
- `/api/v1/pecas` - Gestão de peças em estoque
- `/api/v1/servicos` - Gestão de serviços oferecidos
- `/api/v1/clientes` - Gestão de clientes
- `/api/v1/veiculos` - Gestão de veículos
- `/api/v1/ordensdeservico` - Abertura e acompanhamento de ordens de serviço

### Testando com Insomnia

Uma collection do Insomnia está disponível para facilitar os testes:

1. Abra o Insomnia
2. Clique em **Import** e selecione `oficina-insomnia-collection.json`
3. Configure a variável `base_url` para `http://localhost:8081/api/v1` (já pré-configurada)
4. Faça login no endpoint de **Autenticação** para obter o token JWT
5. Copie o token para a variável `jwt_token` no Environment

## Testes e Qualidade

### Executar Testes

```bash
mvn test
```

### Relatório de Cobertura (JaCoCo)

```bash
mvn verify
```

O relatório será gerado em `target/site/jacoco/index.html`.

A configuração do JaCoCo no `pom.xml` exclui classes de configuração, DTOs, entidades e classes geradas, focando na lógica de negócio (Use Cases e Modelos).

### Análise com SonarQube

Este projeto integra SonarQube para garantir cobertura de testes e segurança.

#### Setup Localmente

**1. Subir o servidor SonarQube via Docker**

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:latest
```

(Aguarde 1-2 minutos para inicialização)

**2. Primeiro Acesso**

- Acesse: **http://localhost:9000**
- Login padrão: `admin` / `admin`
- O sistema pedirá para criar uma nova senha

**3. Criar Projeto e Token**

- Clique em **Create Project > Local (Manually)**
- Project name: `oficina-api`
- Escolha análise **Locally**
- Crie um token (começa com `sqp_`) e copie-o

**4. Executar Análise**

Na raiz do projeto, execute:

```bash
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=oficina-api \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=SEU_TOKEN_AQUI
```

**5. Visualizar Resultados**

Volte a **http://localhost:9000** para ver o dashboard com Security Hotspots, Code Smells e cobertura de testes.

### Dashboard Público

Acesse o relatório completo no SonarCloud:

**[Dashboard SonarCloud](https://sonarcloud.io/summary/overall?id=mateuslourencao_soat-fase1-tech-challenge&branch=main)**

## Licença

Este projeto foi desenvolvido como parte do SOAT Tech Challenge.

## Contribuidores

Mateus Lourenção - [GitHub](https://github.com/mateuslourencao)

Pedro Ruiz - [GitHub](https://github.com/opedro)
