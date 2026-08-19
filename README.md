# 📚 Sistema de Biblioteca — Spring Boot

Aplicação REST desenvolvida em **Java + Spring Boot + PostgreSQL** para gerenciamento de uma biblioteca: cadastro de usuários e livros, controle de empréstimos e devoluções, geração de relatórios e integração com a **Google Books API** para importação de livros.

Projeto desenvolvido como atividade prática de Spring Boot, com foco em APIs REST, JPA/Hibernate, relacionamentos entre entidades, regras de negócio na camada de serviço, transações e consumo de API externa.

---

## ✨ Funcionalidades

### Usuários
- Cadastro, consulta (por id e listagem), atualização e inativação (soft delete)
- Validação de CPF único
- Bloqueio de inativação enquanto houver empréstimos em aberto

### Livros
- CRUD completo (criação, consulta, atualização, exclusão)
- Validação de ISBN único
- Consulta de livros disponíveis e livros sem exemplares disponíveis
- Bloqueio de exclusão enquanto houver empréstimos em aberto

### Empréstimos
- Realização de empréstimo com validação de usuário ativo e disponibilidade do livro
- Registro de devolução, com atualização automática de status e estoque
- Listagem geral, por usuário e de empréstimos em aberto
- Operações transacionais (`@Transactional`), garantindo consistência entre empréstimo e estoque do livro

### Integração com Google Books
- Pesquisa de livros por termo diretamente na Google Books API
- Importação de um livro pesquisado para o acervo da biblioteca, com tratamento de dados ausentes/incompletos vindos da API externa
- Validação de duplicidade de ISBN também na importação
- Tratamento de erros de comunicação com a API externa

### Relatórios (desafio adicional)
- Relatório por usuário: total de empréstimos, empréstimos em aberto e livros devolvidos
- Listagem de livros atualmente emprestados (livro, usuário, data do empréstimo e status)

---

## 🛠️ Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA / Hibernate
- Bean Validation (`jakarta.validation`)
- PostgreSQL
- Lombok
- Maven
- Docker / Docker Compose
- RestTemplate (consumo da Google Books API)

---

## 🏗️ Arquitetura

O projeto é organizado em camadas, separando responsabilidades:

```
model         → entidades JPA (Usuario, Livro, Emprestimo)
repository    → interfaces Spring Data JPA, com queries derivadas
dto           → objetos de transferência de dados (Request/Response), separados da entidade
service       → regras de negócio e orquestração das operações
controller    → endpoints REST, sem lógica de negócio
googlebooks   → integração isolada com a Google Books API (client, service, dtos e controller próprios)
```

A separação entre `Entity` e `DTO` evita expor a estrutura interna do banco de dados na API e previne problemas de serialização (como recursão infinita em relacionamentos bidirecionais).

A integração com a Google Books segue uma responsabilidade dividida:

- **`GoogleBooksClient`** — comunicação HTTP pura com a API externa (sem conhecimento de regras de negócio).
- **`GoogleBooksService`** — regras de negócio, tratamento de dados incompletos e persistência, reaproveitando o `LivroRepository` já existente.

---

## 📋 Modelo de dados

**Usuario** `1 —— N` **Emprestimo** `N —— 1` **Livro**

Cada empréstimo pertence a exatamente um usuário e um livro; um usuário ou livro pode estar associado a diversos empréstimos ao longo do tempo.

---

## 🔌 Endpoints principais

### Usuários (`/usuarios`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/usuarios` | Cadastrar usuário |
| GET | `/usuarios` | Listar usuários |
| GET | `/usuarios/{id}` | Buscar usuário por id |
| PUT | `/usuarios/{id}` | Atualizar usuário |
| DELETE | `/usuarios/{id}` | Inativar usuário |
| GET | `/usuarios/relatorio` | Relatório de empréstimos por usuário |

### Livros (`/livros`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/livros` | Cadastrar livro |
| GET | `/livros` | Listar livros |
| GET | `/livros/{id}` | Buscar livro por id |
| PUT | `/livros/{id}` | Atualizar livro |
| DELETE | `/livros/{id}` | Excluir livro |
| GET | `/livros/disponiveis` | Livros com exemplares disponíveis |
| GET | `/livros/sem-exemplares` | Livros sem exemplares disponíveis |

### Empréstimos (`/emprestimos`)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/emprestimos` | Realizar empréstimo |
| GET | `/emprestimos` | Listar empréstimos |
| GET | `/emprestimos/{id}` | Buscar empréstimo por id |
| GET | `/emprestimos/usuario/{usuarioId}` | Empréstimos de um usuário |
| GET | `/emprestimos/abertos` | Empréstimos em aberto |
| PUT | `/emprestimos/{id}/devolucao` | Registrar devolução |

### Google Books (`/livros/google-books`)
| Método | Rota | Descrição |
|---|---|---|
| GET | `/livros/google-books?nome={nome}` | Pesquisar livros na Google Books |
| POST | `/livros/google-books/{volumeId}/importar` | Importar um livro pesquisado para o acervo |

---

## ⚙️ Regras de negócio

- Não é possível cadastrar dois usuários com o mesmo CPF, nem dois livros com o mesmo ISBN.
- Usuários inativos não podem realizar empréstimos.
- Um livro só pode ser emprestado se houver ao menos um exemplar disponível.
- A quantidade disponível de um livro é ajustada automaticamente a cada empréstimo (-1) e devolução (+1).
- Um empréstimo já devolvido não pode ser devolvido novamente.
- Usuários e livros com empréstimos em aberto não podem ser inativados/excluídos.
- Livros importados da Google Books iniciam com quantidade e quantidade disponível iguais a 1.
- Um livro só é importado da Google Books se ainda não existir um cadastro com o mesmo ISBN.

---

## 🚀 Como executar o projeto

### Pré-requisitos
- Java 17+ instalado
- Docker e Docker Compose instalados
- Maven (ou usar o wrapper `./mvnw` incluso no projeto)

### 1. Subir o banco de dados PostgreSQL via Docker

Na raiz do projeto, onde está o `docker-compose.yml`:

```bash
docker compose up -d
```

Isso sobe um container PostgreSQL, acessível em `localhost:5438`, com o banco `biblioteca` já criado.

### 2. Configurar a aplicação

O `src/main/resources/application.properties` já está configurado para se conectar ao banco do container:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5438/biblioteca
spring.datasource.username=bibliotecas
spring.datasource.password=bibliotecas
spring.jpa.hibernate.ddl-auto=update
```

### 3. Rodar a aplicação

Pela IDE (IntelliJ, por exemplo), execute a classe principal `CadastroDeNinjasApplication`/classe `@SpringBootApplication`, ou via terminal:

```bash
./mvnw spring-boot:run
```

A aplicação sobe por padrão em `http://localhost:8080`.

### 4. Testar os endpoints

Uma coleção do Postman com todos os cenários de teste (sucesso e falha) está disponível junto ao projeto, cobrindo usuários, livros, empréstimos, relatório e integração com Google Books.

---

## 🧪 Cenários de teste cobertos

- Cadastro com sucesso e com dados duplicados (CPF/ISBN)
- Validação de campos obrigatórios (Bean Validation)
- Empréstimo com usuário inativo, livro indisponível e cenários de sucesso
- Devolução simples e devolução duplicada
- Bloqueio de exclusão/inativação com empréstimos em aberto
- Consultas filtradas (disponíveis, sem exemplares, em aberto, por usuário)
- Geração de relatório com contagens corretas
- Pesquisa e importação via Google Books, incluindo tratamento de erro de comunicação (ex: limite de cota da API)

---

## 🔮 Possíveis melhorias futuras

- Substituir `RuntimeException` genérica por exceções de negócio customizadas, com um `@RestControllerAdvice` centralizando o tratamento e retornando os status HTTP semanticamente corretos (404, 409, etc.)
- Adicionar chave de API própria para a Google Books, evitando o limite de cota compartilhado
- Cobertura de testes automatizados (unitários e de integração)
- Migração de `RestTemplate` para `WebClient` (abordagem reativa)

---

## 👤 Autor

Projeto desenvolvido como atividade prática de estudo em Java e Spring Boot.
