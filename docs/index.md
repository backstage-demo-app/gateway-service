# Gateway Service

API Gateway microservice that routes and aggregates requests to downstream services.

## Overview

Gateway Service is a Spring Boot microservice that acts as the single entry point for the storefront-ui frontend. It proxies payment operations to payment-service and provides aggregated dashboard data and service health monitoring.

## Architecture

```
storefront-ui (3001) → gateway-service (8081) → payment-service (8080)
```

Gateway Service sits between the frontend and backend services, providing:

- **Dashboard aggregation** — combines stats + recent payments from payment-service
- **Payment proxy** — forwards create, list, lookup, and refund requests
- **Health monitoring** — checks health of all downstream services

## Quick Start

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Test
mvn test
```

The service starts on **port 8081** by default.

## API Endpoints

| Endpoint | Method | Description |
|---|---|---|
| `/api/gateway/dashboard` | GET | Aggregated dashboard (stats + payments) |
| `/api/gateway/payments` | GET | Proxy: list payments from payment-service |
| `/api/gateway/payments` | POST | Proxy: create payment via payment-service |
| `/api/gateway/payments/{id}` | GET | Proxy: get single payment |
| `/api/gateway/payments/{id}/refund` | POST | Proxy: refund a payment |
| `/api/gateway/services/health` | GET | Health of all downstream services |
| `/actuator/health` | GET | Spring Actuator health check |
| `/actuator/info` | GET | Service info |
| `/actuator/metrics` | GET | Prometheus metrics |
| `/v3/api-docs` | GET | OpenAPI 3 spec (JSON) |
| `/swagger-ui.html` | GET | Swagger UI |

## Configuration

| Property | Default | Description |
|---|---|---|
| `server.port` | `8081` | Server listen port |
| `gateway.payment-service.url` | `http://localhost:8080` | Payment service base URL |

## Owner

This service is owned by **group:default/stargate** and is part of the **demo-platform** system.
