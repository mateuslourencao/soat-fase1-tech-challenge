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
2. Container da aplicação (`oficina-app`) na porta `8080`

A aplicação aguardará o banco de dados estar pronto antes de iniciar.

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

**[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

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
3. Configure a variável `base_url` para `http://localhost:8080/api/v1` (já pré-configurada)
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

