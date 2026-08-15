# Oficina Mecânica - SOAT Tech Challenge

Este projeto é um MVP de um Sistema de Oficina Mecânica, desenvolvido como parte do Tech Challenge da Pós-Graduação em Arquitetura de Software (SOAT). A aplicação gerencia o fluxo administrativo, de estoque e de manutenção de uma oficina.

## 🛠 Tecnologias Utilizadas

- **Java 21**: Linguagem principal.
- **Spring Boot 4.1.0**: Framework para construção da aplicação.
- **Spring Data JPA**: Abstração de persistência de dados.
- **Spring Security & JWT**: Autenticação e autorização.
- **MySQL 8.0**: Banco de dados relacional.
- **Maven**: Gerenciador de dependências e build.
- **Docker & Docker Compose**: Conteinerização da aplicação e banco de dados.
- **SpringDoc OpenAPI**: Documentação da API.
- **Jacoco**: Relatórios de cobertura de testes.

## 🏗 Arquitetura

O projeto segue os princípios da **Arquitetura Hexagonal (Ports and Adapters)**, organizado em módulos de domínio para garantir a separação de responsabilidades e facilitar a testabilidade.

### Módulos Principais:
- **Administrativo**: Gestão de funcionários e autenticação.
- **Estoque**: Controle de peças e serviços.
- **Manutenção**: Gestão de clientes, veículos e ordens de serviço.

## 🚀 Como Executar o Projeto

### Pré-requisitos
- Docker e Docker Compose instalados.
- Java 21 e Maven (caso deseje rodar localmente sem Docker).

### Usando Docker (Recomendado)

Na raiz do projeto, execute o comando:

```bash
docker-compose up --build
```

Isso iniciará:
1. Um container MySQL (`oficina-mysql`) na porta `3306` (inicializado com o `schema.sql`).
2. O container da aplicação (`oficina-app`) na porta `8080`.

A aplicação aguardará o banco de dados estar pronto antes de iniciar (via `restart: on-failure`).

### 🗄️ Estrutura do Banco de Dados
Embora o Hibernate esteja configurado para criar as tabelas automaticamente (`ddl-auto: update`), fornecemos um script SQL completo para referência ou criação manual.
- **Arquivo**: `schema.sql` (na raiz do projeto).
- **Conteúdo**: Definição de tabelas, índices e restrições.

### Executando Localmente

1. Certifique-se de ter um banco MySQL rodando.
2. Configure as variáveis de ambiente necessárias ou altere o `src/main/resources/application.properties`.
   - `DB_URL`: URL de conexão (Ex: `jdbc:mysql://localhost:3306/oficina`)
   - `DB_USERNAME`: Usuário do banco.
   - `DB_PASSWORD`: Senha do banco.
3. Execute via Maven:

```bash
mvn clean spring-boot:run
```

### 📥 Insomnia Collection
Para facilitar os testes dos endpoints, incluímos uma collection do Insomnia na raiz do projeto:
- **Arquivo**: `oficina-insomnia-collection.json`
- **Como usar**:
  1. Abra o Insomnia.
  2. Clique em **Import** e selecione o arquivo.
  3. No ambiente (Environment), a variável `base_url` já está configurada para `http://localhost:8080/api/v1`.
  4. Realize o login no endpoint de **Autenticação** para obter o token.
  5. Copie o token para a variável `jwt_token` no Environment para habilitar a autenticação automática nos demais requests.

## 📖 Documentação da API (Swagger)

Com a aplicação rodando, você pode acessar a documentação interativa e realizar testes nas rotas através do Swagger UI:

🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### Principais Endpoints:
- `/api/v1/administrativo/autenticacao`: Login de funcionários.
- `/api/v1/administrativo/funcionarios`: Gestão de funcionários.
- `/api/v1/pecas`: Gestão de peças em estoque.
- `/api/v1/servicos`: Gestão de serviços oferecidos.
- `/api/v1/clientes`: Gestão de clientes.
- `/api/v1/veiculos`: Gestão de veículos.
- `/api/v1/ordensdeservico`: Abertura e acompanhamento de ordens de serviço.

## 🧪 Testes e Cobertura

Para executar os testes unitários e integrados:

```bash
mvn test
```

Para gerar o relatório de cobertura do Jacoco:

```bash
mvn verify
```
O relatório será gerado em `target/site/jacoco/index.html`.

A configuração do Jacoco no `pom.xml` inclui exclusões para classes de configuração, DTOs, entidades e classes geradas, focando a métrica na lógica de negócio (Use Cases e Modelos).
