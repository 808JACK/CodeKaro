package com.example.demo.controller;

import com.example.demo.dtos.ContestSubmissionRequest;
import com.example.demo.dtos.ContestSubmissionResponse;
import com.example.demo.entities.ContestSubmission;
import com.example.demo.services.SubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class SubmissionController {
    
    private final SubmissionService submissionService;
    
    /**
     * Submit code for a contest problem
     */
    @PostMapping("/submit")
    public ResponseEntity<ContestSubmissionResponse> submitCode(@RequestBody ContestSubmissionRequest request) {
        try {
            log.info("Received code submission for contest {} from user {}", 
                    request.getContestId(), request.getUserId());
            
            ContestSubmission submission = submissionService.saveSubmission(
                request.getContestId(),
                request.getUserId(),
                request.getUserName(),
                request.getProblemId(),
                request.getProblemTitle(),
                request.getCode(),
                request.getLanguage(),
                request.getTimeFromStartMs()
            );
            
            ContestSubmissionResponse response = ContestSubmissionResponse.builder()
                .submissionId(submission.getId())
                .status("SUBMITTED")
                .message("Code submitted successfully")
                .submittedAt(submission.getSubmittedAt())
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error submitting code: {}", e.getMessage(), e);
            ContestSubmissionResponse errorResponse = ContestSubmissionResponse.builder()
                .status("ERROR")
                .message("Failed to submit code: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Get user's submissions for a contest
     */
    @GetMapping("/contest/{contestId}/user/{userId}")
    public ResponseEntity<List<ContestSubmission>> getUserSubmissions(
            @PathVariable Long contestId, 
            @PathVariable Long userId) {
        try {
            List<ContestSubmission> submissions = submissionService.getUserSubmissions(contestId, userId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            log.error("Error fetching user submissions: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get user's submissions for a specific problem
     */
    @GetMapping("/contest/{contestId}/user/{userId}/problem/{problemId}")
    public ResponseEntity<List<ContestSubmission>> getUserProblemSubmissions(
            @PathVariable Long contestId, 
            @PathVariable Long userId,
            @PathVariable Long problemId) {
        try {
            List<ContestSubmission> submissions = submissionService.getUserProblemSubmissions(contestId, userId, problemId);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            log.error("Error fetching user problem submissions: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get user's accepted submission for a problem
     */
    @GetMapping("/contest/{contestId}/user/{userId}/problem/{problemId}/accepted")
    public ResponseEntity<ContestSubmission> getUserAcceptedSubmission(
            @PathVariable Long contestId, 
            @PathVariable Long userId,
            @PathVariable Long problemId) {
        try {
            Optional<ContestSubmission> submission = submissionService.getUserAcceptedSubmission(contestId, userId, problemId);
            if (submission.isPresent()) {
                return ResponseEntity.ok(submission.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error fetching accepted submission: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Update submission with execution results (called by judge service)
     */
    @PutMapping("/{submissionId}/results")
    public ResponseEntity<ContestSubmission> updateSubmissionResults(
            @PathVariable String submissionId,
            @RequestBody ContestSubmissionResponse results) {
        try {
            ContestSubmission updated = submissionService.updateSubmissionResults(
                submissionId,
                results.getIsAccepted(),
                results.getScore(),
                results.getExecutionTimeMs(),
                results.getMemoryUsedKb(),
                results.getStatus(),
                results.getErrorMessage(),
                results.getTestCasesPassed(),
                results.getTotalTestCases()
            );
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error updating submission results: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
