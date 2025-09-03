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
        response.put("service", "CodeKaro Auth Service");
        response.put("status", "Authentication service is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("description", "Handles user authentication, registration, and profile management");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/health");
        endpoints.put("signup", "/auth/signup");
        endpoints.put("login", "/auth/login");
        endpoints.put("profile", "/auth/profiles");
        endpoints.put("token-validation", "/token/**");
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }
}