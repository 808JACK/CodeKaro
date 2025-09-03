package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {
    
    @GetMapping({"/", "/status"})
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "CodeKaro Collaboration Service");
        response.put("status", "Collaboration service is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("description", "Handles contests, code execution, and collaborative programming");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/health");
        endpoints.put("contests", "/collab/api/contests/**");
        endpoints.put("code-execution", "/collab/api/contests/code/run");
        endpoints.put("submissions", "/collab/api/contests/{contestCode}/code/submit");
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }
}