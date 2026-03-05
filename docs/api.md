# API Reference

## Base URL

```
http://localhost:8081
```

## REST Endpoints

### GET /api/gateway/dashboard

Aggregated dashboard combining payment stats and recent transactions from payment-service.

**Response:**

```json
{
  "timestamp": "2026-03-05T18:00:00Z",
  "gateway": "gateway-service",
  "paymentStats": {
    "totalPayments": 3,
    "completed": 2,
    "refunded": 1,
    "totalAmount": 179.49,
    "currency": "USD"
  },
  "recentPayments": {
    "payments": [ ... ],
    "total": 3
  }
}
```

If payment-service is down, each sub-key returns an error object instead.

### GET /api/gateway/payments

Proxy: list payments from payment-service.

**Query Parameters:**

| Parameter | Default | Description |
|---|---|---|
| `status` | `all` | Filter by status (`completed`, `refunded`, `all`) |

**Response:** Same as `GET /api/payments` on payment-service.

**Error (502):**

```json
{
  "error": "Payment service unavailable",
  "details": "Connection refused"
}
```

### POST /api/gateway/payments

Proxy: create a new payment via payment-service.

**Request Body:**

```json
{
  "method": "credit_card",
  "amount": 99.99,
  "currency": "USD",
  "orderId": "order-2001",
  "customerId": "customer-5"
}
```

**Response (201):** Created payment object.

### GET /api/gateway/payments/{id}

Proxy: retrieve a single payment by ID.

### POST /api/gateway/payments/{id}/refund

Proxy: refund a completed payment.

### GET /api/gateway/services/health

Returns live health status of the gateway itself and all downstream services.

**Response:**

```json
{
  "gateway": {
    "status": "UP",
    "port": 8081
  },
  "payment-service": {
    "status": "UP",
    "url": "http://localhost:8080"
  }
}
```

If a downstream service is unreachable:

```json
{
  "payment-service": {
    "status": "DOWN",
    "url": "http://localhost:8080",
    "error": "Connection refused"
  }
}
```

### GET /actuator/health

Spring Boot Actuator health endpoint.

**Response:**

```json
{
  "status": "UP"
}
```

## OpenAPI / Swagger

- **OpenAPI JSON:** `GET /v3/api-docs`
- **OpenAPI YAML:** `GET /v3/api-docs.yaml`
- **Swagger UI:** `GET /swagger-ui.html`
