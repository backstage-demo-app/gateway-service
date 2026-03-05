# Architecture

## Technology Stack

- **Runtime:** Java 17
- **Framework:** Spring Boot 3.3.x
- **Build Tool:** Maven
- **HTTP Client:** Spring RestTemplate
- **API Docs:** springdoc-openapi 2.5.0
- **Container:** Docker (Eclipse Temurin Alpine)

## Project Structure

```
gateway-service/
├── src/
│   └── main/
│       ├── java/com/demo/gatewayservice/
│       │   ├── Application.java          # Main entry point
│       │   ├── GatewayController.java    # Gateway REST API
│       │   └── WebConfig.java            # RestTemplate + CORS config
│       └── resources/
│           └── application.properties    # Configuration
├── docs/                                 # TechDocs documentation
├── Dockerfile                            # Container image
├── pom.xml                               # Maven build
├── catalog-info.yaml                     # Backstage catalog entity
└── mkdocs.yml                            # TechDocs config
```

## Service Dependencies

Gateway Service depends on **payment-service** for all payment data. The downstream URL is configurable via `gateway.payment-service.url`.

```
storefront-ui → gateway-service (this) → payment-service
      :3001            :8081                    :8080
```

## Request Flow

1. **storefront-ui** calls gateway-service endpoints under `/api/gateway/*`
2. **GatewayController** uses `RestTemplate` to proxy/aggregate calls to payment-service
3. If payment-service is down, gateway returns a 502 with error details
4. The `/dashboard` endpoint aggregates both stats and recent payments in a single call

## CORS

WebConfig allows origins from storefront-ui (3001) and Backstage (3000/7007).

## Health & Monitoring

- `/actuator/health` — Liveness/readiness probe
- `/actuator/metrics` — Micrometer metrics
- `/api/gateway/services/health` — Custom endpoint checking all downstream services
- Structured logging with timestamp, thread, level, and logger
