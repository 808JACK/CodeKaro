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
        response.put("service", "CodeKaro Collaboration Service");
        response.put("status", "🤝 Collaboration service is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("description", "Handles contests, code execution, and collaborative programming");
        response.put("endpoints", Map.of(
            "health", "/health",
            "contests", "/collab/api/contests/**",
            "code-execution", "/collab/api/contests/code/run",
            "submissions", "/collab/api/contests/{contestCode}/code/submit"
        ));
        return ResponseEntity.ok(response);
    }
}