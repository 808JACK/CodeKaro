package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {
    
    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "CodeKaro Eureka Discovery Service");
        response.put("status", "🔍 Service discovery is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("description", "Netflix Eureka service registry for microservices discovery");
        response.put("endpoints", Map.of(
            "health", "/health",
            "eureka-dashboard", "/",
            "service-registry", "/eureka/apps"
        ));
        return ResponseEntity.ok(response);
    }
}