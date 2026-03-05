# gateway-service

gateway

## Overview

This is a Spring Boot microservice created from the **Spring Boot Service Template**.

## Quick Start

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run

# Test
mvn test
```

## API

The service exposes the following endpoints:

| Endpoint            | Method | Description             |
| ------------------- | ------ | ----------------------- |
| `/api/hello`        | GET    | Health check / greeting |
| `/actuator/health`  | GET    | Spring Actuator health  |
| `/actuator/info`    | GET    | Service info            |
| `/actuator/metrics` | GET    | Prometheus metrics      |

## Configuration

| Property      | Default              | Description        |
| ------------- | -------------------- | ------------------ |
| `server.port` | `8080` | Server listen port |

## Owner

This service is owned by **group:default/stargate**.
