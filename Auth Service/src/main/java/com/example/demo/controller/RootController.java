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
        response.put("service", "CodeKaro Auth Service");
        response.put("status", "🔐 Authentication service is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("description", "Handles user authentication, registration, and profile management");
        response.put("endpoints", Map.of(
            "health", "/health",
            "signup", "/auth/signup",
            "login", "/auth/login",
            "profile", "/auth/profiles",
            "token-validation", "/token/**"
        ));
        return ResponseEntity.ok(response);
    }
}