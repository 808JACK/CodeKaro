package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {
    
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "CodeKaro API Gateway");
        response.put("status", "🚀 Service is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("description", "Main entry point for CodeKaro microservices");
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/health");
        endpoints.put("ping", "/ping");
        endpoints.put("auth", "/auth/**");
        endpoints.put("problems", "/problem/**");
        endpoints.put("collaboration", "/collab/**");
        response.put("endpoints", endpoints);
        return ResponseEntity.ok(response);
    }
}