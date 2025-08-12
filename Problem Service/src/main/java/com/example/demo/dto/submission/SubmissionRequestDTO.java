package com.example.demo.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRequestDTO {
    private UUID problemId;          // Problem being submitted for
    private String language;         // Programming language used
    private String code;            // User's solution code
} 