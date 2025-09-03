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
        response.put("service", "CodeKaro Problem Service");
        response.put("status", "🧩 Problem service is running");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        response.put("description", "Manages coding problems, topics, and problem details");
        response.put("endpoints", Map.of(
            "health", "/health",
            "topics", "/problem/topicList",
            "problems-by-topic", "/problem/topic/{topic}",
            "problem-details", "/problem/{problemId}"
        ));
        return ResponseEntity.ok(response);
    }
}