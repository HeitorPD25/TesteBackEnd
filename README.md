# 📝 README - Sistema de Gestão de Tarefas e Pessoas

## 📌 Visão Geral
Sistema Spring Boot para gerenciamento de:
- **Pessoas** (cadastro, alocação em departamentos)
- **Tarefas** (criação, alocação, finalização)
- Relacionamento entre pessoas e tarefas

## 🛠 Tecnologias
- **Java 17**
- **Spring Boot 3.5.4**
- **Spring Data JPA**
- **H2 Database** (em memória)
- **PostgreSQL** (para produção)
- **Lombok**
- **Mockito/JUnit 5** (testes)

## 📋 Endpoints Principais

### 👥 Pessoas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/pessoas` | Cria nova pessoa |
| PUT | `/pessoas/{id}` | Atualiza pessoa existente |
| GET | `/pessoas` | Lista pessoas com resumo de horas |
| GET | `/pessoas/gastos` | Consulta gastos por período |

### ✅ Tarefas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/tarefas` | Cria nova tarefa |
| PUT | `/tarefas/alocar/{id}` | Aloca pessoa à tarefa |
| PUT | `/tarefas/finalizar/{id}` | Finaliza tarefa |
| GET | `/tarefas/pendentes` | Lista tarefas pendentes |

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.8+
- PostgreSQL (opcional)

### Passo a Passo
1. Clone o repositório:
   ```bash
   git clone https://github.com/HeitorPD25/TesteBackEnd.git
   cd TesteBackEnd

### ✉️ Contato

## Email: 
heitorpdte@gmail.com
## GitHub: 
@HeitorPD25



