# Baozi Store API

API REST desenvolvida com **Java 21** e **Spring Boot 3** para gerenciamento de clientes, produtos e pedidos da **Baozi Store**. O projeto utiliza **Spring Data JPA** para persistência de dados em banco **MySQL 8.4** executado via contêiner **Podman (rootless)**.

---

## 📋 Sumário
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Arquitetura do Projeto](#-arquitetura-do-projeto)
- [Requisitos Prévios](#-requisitos-prévios)
- [Configuração do Banco de Dados (Podman)](#-configuração-do-banco-de-dados-podman)
- [Como Executar a Aplicação](#-como-executar-a-aplicação)
- [Endpoints da API](#-endpoints-da-api)
- [Testes Automatizados e Coleção Postman](#-testes-automatizados-e-coleção-postman)
- [Autor e Contexto Acadêmico](#-autor-e-contexto-acadêmico)

---

## 🛠 Tecnologias Utilizadas

* **Linguagem:** Java 21 LTS
* **Framework:** Spring Boot 3.x
  * Spring Web (REST)
  * Spring Data JPA
  * Bean Validation (`spring-boot-starter-validation`)
* **Banco de Dados:** MySQL 8.4 LTS
* **Containerização:** Podman (Rootless Container Engine)
* **Gerenciador de Dependências:** Apache Maven
* **Testes de API:** Postman

---

## 📐 Arquitetura do Projeto

O código-fonte segue a arquitetura em camadas do padrão MVC no pacote base `com.baozistore.api`:

```text
src/main/java/com/baozistore/api/
├── config/         # Configurações globais (CORS, datas, etc.)
├── controller/     # Controllers REST (Exposição dos endpoints HTTP)
├── exception/      # Tratamento global de exceções e respostas de erro
├── model/          # Entidades JPA (Mapeamento de tabelas MySQL)
├── repository/     # Interfaces Spring Data JPA
└── service/        # Regras de negócio e validações das operações
```

---

## 🖥 Requisitos Prévios

Antes de iniciar, certifique-se de ter instalado em sua máquina:
* **JDK 21** configurado no `PATH`
* **Apache Maven 3.8+**
* **Podman** (ou Docker Engine)
* **Postman** (para execução da suíte de testes)

---

## 🗄 Configuração do Banco de Dados (Podman)

### 1. Criar Rede e Volume
```bash
podman network create baozi-net
podman volume create baozi-mysql-data
```

### 2. Inicializar o Contêiner MySQL 8.4
```bash
podman run -d --name baozi-mysql \
  --network baozi-net \
  -p 127.0.0.1:3306:3306 \
  -e MYSQL_ROOT_PASSWORD=RootBaozi@2026 \
  -e MYSQL_DATABASE=baozi_store \
  -e MYSQL_USER=baozi_app \
  -e MYSQL_PASSWORD=AppBaozi@2026 \
  -v baozi-mysql-data:/var/lib/mysql:Z \
  docker.io/library/mysql:8.4
```

### 3. Limpeza de Dados (Reset de ID AUTO_INCREMENT)
Caso precise reiniciar os testes garantindo os contadores zerados:
```bash
podman exec -e MYSQL_PWD=AppBaozi@2026 baozi-mysql mysql -u baozi_app baozi_store \
  -e "TRUNCATE TABLE pedidos; TRUNCATE TABLE produtos; TRUNCATE TABLE clientes;"
```

---

## 🚀 Como Executar a Aplicação

### Configuração de Ambiente (`src/main/resources/application.properties`)
```properties
spring.application.name=baozi-store-api
server.port=8080

# Conexão MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/baozi_store?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=baozi_app
spring.datasource.password=AppBaozi@2026
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Configurações do Hibernate / JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

### Executar a API via Maven
Na raiz do projeto, rode:
```bash
mvn spring-boot:run
```
A API estará acessível em `http://localhost:8080`.

---

## 📍 Endpoints da API

### 👤 Clientes (`/api/clientes`)

| Método | Endpoint | Descrição | Corpo da Requisição (JSON) | Status |
| :---: | :--- | :--- | :--- | :---: |
| `POST` | `/api/clientes` | Cadastra um novo cliente | `{"nome": "Rafael5239095", "clienteDesde": "2026-11-24"}` | `201` |
| `GET` | `/api/clientes` | Lista todos os clientes | N/A | `200` |
| `GET` | `/api/clientes/{id}` | Busca cliente por ID | N/A | `200` |
| `PUT` | `/api/clientes/{id}` | Atualiza cliente por ID | `{"nome": "Rafael Prado", "clienteDesde": "2026-11-24"}` | `200` |
| `DELETE` | `/api/clientes/{id}` | Apaga cliente por ID | N/A | `204` |

---

### 🥟 Produtos (`/api/produtos`)

| Método | Endpoint | Descrição | Corpo da Requisição (JSON) | Status |
| :---: | :--- | :--- | :--- | :---: |
| `POST` | `/api/produtos` | Cadastra um novo produto | `{"nome": "Baozi Tradicional de Carne Suína", "preco": 12.50, "estoque": true}` | `201` |
| `GET` | `/api/produtos` | Lista todos os produtos | N/A | `200` |
| `GET` | `/api/produtos/{id}` | Busca produto por ID | N/A | `200` |
| `PUT` | `/api/produtos/{id}` | Atualiza produto por ID | `{"nome": "Baozi de Legumes", "preco": 10.00, "estoque": true}` | `200` |
| `DELETE` | `/api/produtos/{id}` | Apaga produto por ID | N/A | `204` |

---

### 🛒 Pedidos (`/api/pedidos`)

| Método | Endpoint | Descrição | Corpo da Requisição (JSON) | Status |
| :---: | :--- | :--- | :--- | :---: |
| `POST` | `/api/pedidos` | Registra um novo pedido | `{"clienteId": 1, "produtoId": 1, "quantidade": 3}` | `201` |
| `GET` | `/api/pedidos` | Lista todos os pedidos | N/A | `200` |
| `GET` | `/api/pedidos/{id}` | Busca pedido por ID | N/A | `200` |
| `PUT` | `/api/pedidos/{id}` | Atualiza pedido por ID | `{"clienteId": 1, "produtoId": 1, "quantidade": 5}` | `200` |
| `DELETE` | `/api/pedidos/{id}` | Apaga pedido por ID | N/A | `204` |

---

## 🧪 Testes Automatizados e Coleção Postman

A suíte de testes de integração e automação com Postman está arquivada no diretório `docs/postman/`.

### Como Importar no Postman:
1. Abra o Postman.
2. Clique em **Import**.
3. Selecione os arquivos:
   * `docs/postman/Baozi_Store_API.postman_collection.json`
   * `docs/postman/Baozi_Store_Local.postman_environment.json`
4. Selecione o ambiente **Baozi Store - Local** no canto superior direito do Postman antes de executar a coleção.

---

## 👨‍💻 Autor e Contexto Acadêmico

* **Estudante:** Rafael Prado de Jesus
* **RU:** `5239095`
* **Instituição:** Centro Universitário Internacional UNINTER
* **Disciplina:** Desenvolvimento Web Back-End
* **Professora:** Luciane Kanashiro, Me.