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
        response.put("service", "CodeKaro Problem Service");
        response.put("status", "Problem service is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("description", "Manages coding problems, topics, and problem details");
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("health", "/health");
        endpoints.put("topics", "/problem/topicList");
        endpoints.put("problems-by-topic", "/problem/topic/{topic}");
        endpoints.put("problem-details", "/problem/{problemId}");
        response.put("endpoints", endpoints);
        
        return ResponseEntity.ok(response);
    }
}