# API Reference

## REST Endpoints

### GET /api/hello

Returns a greeting message with service status.

**Response:**

```json
{
  "service": "gateway-service",
  "message": "Hello from gateway-service!",
  "status": "running"
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

## Error Handling

All errors follow standard Spring Boot error response format:

```json
{
  "timestamp": "2026-01-01T00:00:00.000+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/api/..."
}
```
