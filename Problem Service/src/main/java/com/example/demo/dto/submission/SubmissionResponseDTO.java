package com.example.demo.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponseDTO {
    private UUID submissionId;
    private UUID problemId;
    private String status;          // "ACCEPTED", "WRONG_ANSWER", "TIME_LIMIT_EXCEEDED", etc.
    private String language;
    private ZonedDateTime submittedAt;
    private Long executionTimeMs;
    private Long memoryUsageKb;
    private List<TestCaseResultDTO> sampleTestResults;  // Results for sample test cases
    private String errorMessage;    // Compilation/runtime error if any
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class TestCaseResultDTO {
    private String status;         // "PASSED" or "FAILED"
    private String input;          // Only for sample test cases
    private String expectedOutput; // Only for sample test cases
    private String actualOutput;   // Only for sample test cases
    private Long executionTimeMs;
    private Long memoryUsageKb;
} 