# 🏥 Tech Challenge - Fase 3 | Hospital API

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

![alt text](https://github.com/Pamt-o/tech-challenge-fase3/blob/master/docs/img/arquitetura.png)

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
