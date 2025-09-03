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
        response.put("service", "CodeKaro API Gateway");
        response.put("status", "🚀 Service is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("description", "Main entry point for CodeKaro microservices");
        response.put("endpoints", Map.of(
            "health", "/health",
            "ping", "/ping",
            "auth", "/auth/**",
            "problems", "/problem/**",
            "collaboration", "/collab/**"
        ));
        return ResponseEntity.ok(response);
    }
}