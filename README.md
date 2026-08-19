# 🏥 Hospital API

API REST desenvolvida em **Spring Boot** para controle de internações hospitalares — gerenciamento de hospitais, alas, quartos, leitos, pacientes e internações, com relatórios operacionais.

## 📋 Sobre o projeto

Sistema que permite:
- Cadastrar hospitais, com suas alas, quartos e leitos (inclusive tudo em uma única requisição)
- Gerenciar internação e alta de pacientes
- Consultar relatórios sobre ocupação, disponibilidade e histórico de internações

## 🚀 Tecnologias

- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA** (Hibernate)
- **Spring Validation**
- **PostgreSQL**
- **Docker / Docker Compose**
- **Lombok**
- **Maven**

## 🏗️ Arquitetura

Projeto estruturado em camadas:

```
model/       → Entidades JPA
repository/  → Interfaces JpaRepository
dto/         → Objetos de transferência (Request/Response), organizados por entidade
service/     → Regras de negócio
controller/  → Endpoints REST
enums/       → Enumerações (status)
```

### Modelo de dados

```
Hospital (1) ──< Ward (1) ──< Room (1) ──< Bed
                                              │
Patient ──< AdmissionLog >── Bed
```

- **Hospital**: id, name, phone, cnpj
- **Ward** (ala): id, specialty, hospital
- **Room** (quarto): id, roomCode, status, ward
- **Bed** (leito): id, bedNumber, status, room
- **Patient**: id, name, cpf, phone
- **AdmissionLog** (internação): id, bed, patient, date, status, admissionAt, dischargeAt

## ⚙️ Como rodar o projeto

### Pré-requisitos
- Java 21+
- Docker e Docker Compose
- Maven (ou usar o wrapper `./mvnw`)

### 1. Suba o banco de dados

```bash
docker compose up -d
```

Isso inicia um container PostgreSQL na porta `5434`.

### 2. Configure a conexão

O arquivo `src/main/resources/application.properties` já vem configurado para o banco do Docker Compose:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5434/hospital
spring.datasource.username=hospitais
spring.datasource.password=hospitais
```

### 3. Rode a aplicação

```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`.

## 📡 Endpoints principais

### Hospital
| Método | Rota | Descrição |
|---|---|---|
| POST | `/hospitals` | Cria hospital (pode incluir alas, quartos e leitos aninhados) |
| GET | `/hospitals` | Lista todos os hospitais |
| GET | `/hospitals/{id}` | Busca hospital por ID |
| PUT | `/hospitals/{id}` | Atualiza hospital |
| DELETE | `/hospitals/{id}` | Remove hospital |

### Ward (Ala)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/wards` | Cria ala (pode incluir quartos e leitos) |
| GET | `/wards` | Lista todas as alas |
| GET | `/wards/{id}` | Busca ala por ID |
| PUT | `/wards/{id}` | Atualiza ala |
| DELETE | `/wards/{id}` | Remove ala |

### Room (Quarto)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/rooms` | Cria quarto (código gerado automaticamente, ex: `CARD-1`) |
| GET | `/rooms` | Lista todos os quartos |
| GET | `/rooms/{id}` | Busca quarto por ID |
| PUT | `/rooms/{id}` | Atualiza quarto |
| DELETE | `/rooms/{id}` | Remove quarto |
| GET | `/rooms/available` | Relatório: quartos com leito disponível |
| GET | `/rooms/quantity` | Relatório: quantidade de quartos livres/ocupados/total por especialidade |

### Bed (Leito)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/beds` | Cria leito (status inicial `UNOCCUPIED`) |
| GET | `/beds` | Lista todos os leitos |
| GET | `/beds/{id}` | Busca leito por ID |
| PUT | `/beds/{id}` | Atualiza leito |
| DELETE | `/beds/{id}` | Remove leito |
| GET | `/beds/available?specialty={especialidade}` | Relatório: leitos livres por especialidade |

### Patient (Paciente)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/patient` | Cria paciente |
| GET | `/patient` | Lista todos os pacientes |
| GET | `/patient/{id}` | Busca paciente por ID |
| PUT | `/patient/{id}` | Atualiza paciente |
| DELETE | `/patient/{id}` | Remove paciente |

### Admission (Internação)
| Método | Rota | Descrição |
|---|---|---|
| POST | `/admissions` | Interna um paciente (verifica disponibilidade do leito) |
| PUT | `/admissions/{id}` | Dá alta ao paciente (leito vai para `IN_PREPARATION`) |
| GET | `/admissions/roomPatient/{patientId}` | Relatório: quarto onde o paciente está internado |
| GET | `/admissions/currentAdmission/{patientId}` | Relatório: dados completos da internação atual |
| GET | `/admissions/admissionHistory/{patientId}?page=0&size=10` | Relatório: histórico paginado de internações do paciente |
| GET | `/admissions/activePatients` | Relatório: pacientes internados agora, agrupados por especialidade |
| GET | `/admissions/bedHistory/{bedId}` | Relatório: histórico de internações de um leito |

## 🔄 Regras de negócio

- Leitos são criados com status `UNOCCUPIED`
- Ao internar um paciente, o leito é validado como disponível e muda para `OCCUPIED`
- Ao dar alta, o leito muda para `IN_PREPARATION` (não volta direto para `UNOCCUPIED`)
- Códigos de quarto são gerados automaticamente: `{4 primeiras letras da especialidade}-{número sequencial}` (ex: `CARD-1`, `PEDI-2`)
- Não é possível excluir hospitais/alas/quartos que possuam registros filhos cadastrados
- Não é possível excluir um leito ocupado

## 🧪 Testando a API

Uma coleção do Postman com todos os endpoints está disponível para importação e testes manuais.

## 📌 Status do projeto

✅ Projeto funcional e testado — CRUD completo de todas as entidades, relatórios operacionais e fluxo de internação/alta validados manualmente via Postman.
