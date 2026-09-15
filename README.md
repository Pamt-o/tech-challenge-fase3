# 🏥 Tech Challenge - Fase 3 | Agendamento de consultas

Sistema de agendamento de consultas com autenticação básica, GraphQL e comunicação assíncrona via RabbitMQ.

---

## 📋 Sobre o Projeto

Este projeto foi desenvolvido como parte do **Tech Challenge da Fase 3** do curso de Arquitetura e Desenvolvimento Java. O objetivo é criar um backend simplificado e modular para um ambiente hospitalar, com foco em:

- **Segurança** (Spring Security + Basic Auth)
- **GraphQL** (consultas flexíveis sobre histórico médico)
- **Comunicação Assíncrona** (RabbitMQ)
- **Escalabilidade** (microsserviços)
---
## 🏗️ Arquitetura da Solução

![alt text](https://github.com/Pamt-o/tech-challenge-fase3/blob/master/docs/img/arquitetura_solucao.png)

---
## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
| :--- | :--- | :--- |
| **Java** | 21 | Linguagem principal |
| **Spring Boot** | 3.3.4 | Framework principal |
| **Spring Security** | 6.3.3 | Autenticação e autorização |
| **Spring GraphQL** | 1.3.3 | API GraphQL |
| **Spring Data JPA** | 3.3.4 | Persistência de dados |
| **Spring AMQP** | 3.1.7 | Comunicação com RabbitMQ |
| **PostgreSQL** | 16 | Banco de dados |
| **RabbitMQ** | 3.13 | Mensageria |
| **Docker** | - | Containerização |
| **Lombok** | - | Redução de boilerplate |

---

## 🚀 Como Baixar e Executar

- Docker e Docker Compose instalados
- Git instalado

### Passo 1: Clonar o repositório
```bash
git clone https://github.com/Pamt-o/tech-challenge-fase3

cd tech-challenge-fase3
```

### Passo 2: Subir os containers
```bash
docker-compose up -d --build
```

### Passo 3:  Verificar se está tudo rodando
```bash
docker-compose ps
```
### Saída esperada:

| NAME                        | IMAGE                                          | STATUS | PORTS  |
|:----------------------------|:-----------------------------------------------|:--|---|
| **agendamento-app**         | tech-challenge-fase3-service-agendamento       | Up| 0.0.0.0:8080->8080/tcp  |
| **notificacao-app**         | tech-challenge-fase3-service-notificacao       | Up|  0.0.0.0:8081->8080/tcp |
| **postgres-tech-challenge** | postgres:16-alpine                             | Up | 0.0.0.0:5432->5432/tcp  |
| **rabbitmq-tech-challenge** | rabbitmq:3.13-management-alpine                | Up | 0.0.0.0:5672->5672/tcp, 0.0.0.0:15672->15672/tcp  |


### Passo 4: Verificar os logs

```bash
docker-compose logs -f service-agendamento
```

```bash
docker-compose logs -f service-notificacao
```

---
## 🧪 Collections para Teste

O projeto inclui duas collections do Postman para facilitar os testes:

### 📁 REST — Cadastro de Usuários

Localização na pasta: `docs/collections/COLLECTION 1 -  REST.postman_collection.json`

**Como usar:**
1. Importe no Postman (File → Import)
2. Execute na ordem:
    - Cadastrar Médico
    - Cadastrar Enfermeiro
    - Cadastrar Paciente
    - Listar Usuários (com Basic Auth)

### 📁 GraphQL — Consultas

Localização na pasta: `docs/collections/COLLECTION 2 - GraphQL.postman_collection.json`

**Como usar:**
1. Importe no Postman
2. Execute as queries e mutations
---
### 📚 Documentação Complementar

Para **detalhes aprofundados** sobre a arquitetura, decisões técnicas, modelagem de domínio, fundamentação teórica e decisões arquiteturais (ADRs), consulte o documento:

Localização na pasta : `docs\documentacao\DOCUMENTACAO-TECNICA.pdf`

Ele contém:
- Fundamentação teórica (microsserviços, GraphQL, mensageria, Spring Security);
- Diagramas C4 (contexto e fluxo);
- Modelagem de domínio detalhada;
- Matriz de autorização e níveis de acesso;
- Decisões arquiteturais justificadas (ADRs);
- Ciclo de vida de uma consulta e fluxo assíncrono;
---
## 🎯 Conclusão

O **Hospital API** implementa todos os requisitos do Tech Challenge da Fase 3, integrando **Spring Security (Basic Auth + RBAC)**, **GraphQL**, **microsserviços** e **comunicação assíncrona com RabbitMQ** em um ambiente containerizado com **Docker Compose**. O projeto está funcional, documentado e pronto para avaliação.

