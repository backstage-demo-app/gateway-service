# Architecture

## Technology Stack

- **Runtime:** Java 17
- **Framework:** Spring Boot 3.3.x
- **Build Tool:** Maven
- **Container:** Docker (Eclipse Temurin Alpine)

## Project Structure

```
gateway-service/
├── src/
│   └── main/
│       ├── java/com/demo/
│       │   ├── Application.java        # Main entry point
│       │   └── HelloController.java    # REST controller
│       └── resources/
│           └── application.properties  # Configuration
├── docs/                               # TechDocs documentation
├── Dockerfile                          # Container image
├── pom.xml                             # Maven build
├── catalog-info.yaml                   # Backstage catalog entity
└── mkdocs.yml                          # TechDocs config
```

## CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/ci.yml`):

1. **Build** — Compile and package with Maven
2. **Test** — Run unit tests
3. **Docker** — Build container image (on `main` branch only)

## Health & Monitoring

- `/actuator/health` — Liveness/readiness probe
- `/actuator/metrics` — Micrometer metrics
- Structured logging with timestamp, thread, level, and logger
