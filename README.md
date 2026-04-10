# Microservices — Order + Payment

Dois serviços independentes em **Java 21 + Quarkus 3** que se comunicam via HTTP REST, simulando um fluxo real de pedidos e pagamentos.

---

## Estrutura do Projeto

```
/order-service    → API de pedidos       (porta 8080)
/payment-service  → API de pagamentos    (porta 8081)
/docs             → Documentação do desafio
```

---

## Tecnologias

- Java 21
- Quarkus 3.34
- Hibernate ORM com Panache
- MySQL (provisionado automaticamente via DevServices/Docker)
- MicroProfile REST Client (comunicação entre serviços)
- SmallRye OpenAPI + Swagger UI
- Bean Validation

---

## Como Rodar

### Pré-requisitos

- Java 21+
- Maven (ou use o wrapper `mvnw` incluso)
- Docker rodando (para o MySQL via DevServices)

### Iniciar o Payment Service

```bash
cd payment-service
./mvnw quarkus:dev
```

### Iniciar o Order Service (em outro terminal)

```bash
cd order-service
./mvnw quarkus:dev
```

> **Importante:** O Payment Service deve estar rodando antes de processar pagamentos pelo Order Service.

---

## Swagger UI

| Serviço         | URL                                |
| --------------- | ---------------------------------- |
| Order Service   | http://localhost:8080/q/swagger-ui |
| Payment Service | http://localhost:8081/q/swagger-ui |

---

## Endpoints

### Order Service — `http://localhost:8080`

| Método   | Rota                  | Descrição                                    |
| -------- | --------------------- | -------------------------------------------- |
| `GET`    | `/orders/list`        | Listar pedidos (paginado, filtro por status) |
| `GET`    | `/orders/list/{id}`   | Buscar pedido por ID                         |
| `POST`   | `/orders/create`      | Criar pedido                                 |
| `PUT`    | `/orders/{id}/pay`    | Processar pagamento do pedido                |
| `PUT`    | `/orders/{id}/cancel` | Cancelar pedido                              |
| `DELETE` | `/orders/{id}`        | Remover pedido                               |

**Query params de listagem:**

- `page` (default: 0)
- `pageSize` (default: 10)
- `status` (opcional: `CREATED`, `PAID`, `CANCELED`)

**Body do POST `/orders/create`:**

```json
{
  "description": "Pedido de teste",
  "value": 150
}
```

**Status do pedido:**

- `CREATED` → pedido criado, aguardando pagamento
- `PAID` → pagamento aprovado
- `CANCELED` → pagamento recusado ou pedido cancelado

---

### Payment Service — `http://localhost:8081`

| Método | Rota                        | Descrição                                       |
| ------ | --------------------------- | ----------------------------------------------- |
| `GET`  | `/payments/list`            | Listar pagamentos (paginado, filtro por status) |
| `GET`  | `/payments/list/{id}`       | Buscar pagamento por ID                         |
| `GET`  | `/payments/order/{orderId}` | Buscar pagamentos por pedido                    |
| `POST` | `/payments/create`          | Criar pagamento                                 |
| `PUT`  | `/payments/{id}/approve`    | Aprovar pagamento                               |
| `PUT`  | `/payments/{id}/reject`     | Rejeitar pagamento                              |

**Query params de listagem:**

- `page` (default: 0)
- `pageSize` (default: 10)
- `status` (opcional: `PENDING`, `APPROVED`, `REJECTED`)

**Body do POST `/payments/create`:**

```json
{
  "orderId": "uuid-do-pedido",
  "value": 150
}
```

**Status do pagamento:**

- `PENDING` → aguardando processamento
- `APPROVED` → pagamento aprovado
- `REJECTED` → pagamento rejeitado

---

## Fluxo de Integração

```
1. Cliente cria um pedido        →  POST /orders/create         (status: CREATED)
2. Cliente solicita pagamento    →  PUT  /orders/{id}/pay
   2.1 Order Service cria pagamento no Payment Service  →  POST /payments/create   (status: PENDING)
   2.2 Order Service aprova pagamento                   →  PUT  /payments/{id}/approve (status: APPROVED)
   2.3 Se aprovado → pedido fica PAID
       Se falhou   → pedido fica CANCELED
3. Se Payment Service estiver fora do ar → retorna erro "Serviço de pagamento indisponível"
```

---

## Validações

Todos os campos obrigatórios são validados via Bean Validation. Exemplos de erro (HTTP 400):

- `"A descrição é obrigatória"`
- `"O valor é obrigatório"`
- `"O valor deve ser maior que zero"`
- `"Somente pedidos com status CREATED podem ser cancelados"`
- `"Somente pedidos com status CREATED podem ser removidos"`

---

## Tratamento de Erros

| Código | Situação                                  |
| ------ | ----------------------------------------- |
| `400`  | Dados inválidos ou operação não permitida |
| `404`  | Pedido ou pagamento não encontrado        |
| `502`  | Serviço de pagamento indisponível         |

---

## Banco de Dados

O MySQL é provisionado automaticamente pelo **Quarkus DevServices** via Docker. Não é necessário instalar ou configurar nada manualmente. As tabelas são criadas automaticamente (`drop-and-create`).

| Serviço         | Tabela        |
| --------------- | ------------- |
| Order Service   | `tb_orders`   |
| Payment Service | `tb_payments` |

---

## Responsáveis

- **Giovanni** → Order Service
- **Paulo** → Payment Service
