package com.example.demo.dto.problem;

import com.example.demo.entities.postgres.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemResponseDTO {
    // Problem metadata
    private Long id;
    private String title;
    private Difficulty difficulty;
    private List<Long> topicIds;
    private ZonedDateTime createdAt;

    // Problem content
    private String content;           // Problem statement
    private String constraints;       // Constraints and limits
    private String inputFormat;       // Input format description
    private String outputFormat;      // Output format description
    private List<Map<String, String>> examples;  // Sample test cases with explanations
    
    // Code template for the selected language
    private String template;          // Code template
    private String functionName;      // Name of function to implement
    private String methodSignature;   // Full method signature
    private List<String> supportedLanguages;  // List of available languages
} 