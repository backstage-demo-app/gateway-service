package com.demo.gatewayservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final RestTemplate restTemplate;
    private final String paymentServiceUrl;

    public GatewayController(
            RestTemplate restTemplate,
            @Value("${gateway.payment-service.url:http://localhost:8080}") String paymentServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentServiceUrl = paymentServiceUrl;
    }

    /**
     * Dashboard — aggregates data from downstream services
     */
    @GetMapping("/dashboard")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("timestamp", Instant.now().toString());
        dashboard.put("gateway", "gateway-service");

        // Fetch payment stats
        try {
            Map<String, Object> stats = restTemplate.getForObject(
                    paymentServiceUrl + "/api/payments/stats", Map.class);
            dashboard.put("paymentStats", stats);
        } catch (Exception e) {
            dashboard.put("paymentStats", Map.of("error", "Payment service unavailable: " + e.getMessage()));
        }

        // Fetch recent payments
        try {
            Map<String, Object> payments = restTemplate.getForObject(
                    paymentServiceUrl + "/api/payments?status=all", Map.class);
            dashboard.put("recentPayments", payments);
        } catch (Exception e) {
            dashboard.put("recentPayments", Map.of("error", "Payment service unavailable: " + e.getMessage()));
        }

        return ResponseEntity.ok(dashboard);
    }

    /**
     * Proxy — list payments from payment-service
     */
    @GetMapping("/payments")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> listPayments(
            @RequestParam(defaultValue = "all") String status) {
        try {
            Map<String, Object> result = restTemplate.getForObject(
                    paymentServiceUrl + "/api/payments?status=" + status, Map.class);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body(Map.of("error", "Payment service unavailable", "details", e.getMessage()));
        }
    }

    /**
     * Proxy — get a single payment
     */
    @GetMapping("/payments/{id}")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> getPayment(@PathVariable Long id) {
        try {
            Map<String, Object> result = restTemplate.getForObject(
                    paymentServiceUrl + "/api/payments/" + id, Map.class);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body(Map.of("error", "Payment service unavailable", "details", e.getMessage()));
        }
    }

    /**
     * Proxy — create a new payment via payment-service
     */
    @PostMapping("/payments")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> result = restTemplate.postForObject(
                    paymentServiceUrl + "/api/payments", request, Map.class);
            return ResponseEntity.status(201).body(result);
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body(Map.of("error", "Payment service unavailable", "details", e.getMessage()));
        }
    }

    /**
     * Proxy — refund a payment via payment-service
     */
    @PostMapping("/payments/{id}/refund")
    @SuppressWarnings("unchecked")
    public ResponseEntity<Map<String, Object>> refundPayment(@PathVariable Long id) {
        try {
            Map<String, Object> result = restTemplate.postForObject(
                    paymentServiceUrl + "/api/payments/" + id + "/refund", null, Map.class);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body(Map.of("error", "Payment service unavailable", "details", e.getMessage()));
        }
    }

    /**
     * Health of all downstream services
     */
    @GetMapping("/services/health")
    public ResponseEntity<Map<String, Object>> servicesHealth() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("gateway", Map.of("status", "UP", "port", 8081));

        try {
            restTemplate.getForObject(paymentServiceUrl + "/actuator/health", Map.class);
            health.put("payment-service", Map.of("status", "UP", "url", paymentServiceUrl));
        } catch (Exception e) {
            health.put("payment-service", Map.of("status", "DOWN", "url", paymentServiceUrl, "error", e.getMessage()));
        }

        return ResponseEntity.ok(health);
    }
}
