/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestHeader
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.example.demo.controller;

import com.example.demo.dtos.CodeExecutionRequest;
import com.example.demo.dtos.CodeExecutionResponse;
import com.example.demo.dtos.ContestDetailsResponse;
import com.example.demo.dtos.CreateContestRequest;
import com.example.demo.dtos.RecentRoomResponse;
import com.example.demo.dtos.RoomCreatedResponse;
import com.example.demo.entities.Contest;
import com.example.demo.services.CodeExecutionService;
import com.example.demo.services.ContestService;
import com.example.demo.services.SubmissionService;
import com.example.demo.entities.ContestSubmission;
import com.example.demo.entities.Contest;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value={"/api/contests"})
public class ContestController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ContestController.class);
    private final ContestService contestService;
    private final SubmissionService submissionService;
    @Autowired(required = false)
    private CodeExecutionService codeExecutionService;

    public ContestController(ContestService contestService, SubmissionService submissionService) {
        this.contestService = contestService;
        this.submissionService = submissionService;
    }

    @PostMapping(value = {"/create"})
    public ResponseEntity<RoomCreatedResponse> createContest(@RequestBody CreateContestRequest request, @RequestHeader(value = "X-User-Id") Long userId) {
        try {
            log.info("Creating contest for user {} with {} problems", (Object) userId, (Object) (request.getProblemIds() != null ? request.getProblemIds().size() : 0));
            RoomCreatedResponse response = this.contestService.createContest(request, userId);
            return ResponseEntity.ok((RoomCreatedResponse) response);
        } catch (Exception e) {
            log.error("Error creating contest: {}", (Object) e.getMessage(), (Object) e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(value = {"/{contestCode}/join"})
    public ResponseEntity<ContestDetailsResponse> joinContest(@PathVariable String contestCode, @RequestHeader(value = "X-User-Id") Long userId) {
        try {
            log.info("User {} attempting to join contest {}", (Object) userId, (Object) contestCode);
            if (!this.contestService.isValidContest(contestCode)) {
                return ResponseEntity.notFound().build();
            }
            ContestDetailsResponse response = this.contestService.joinContest(contestCode, userId);
            log.info("User {} successfully joined contest {}", (Object) userId, (Object) contestCode);
            return ResponseEntity.ok((ContestDetailsResponse) response);
        } catch (Exception e) {
            log.error("Error joining contest: {}", (Object) e.getMessage(), (Object) e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = {"/{contestCode}/details"})
    public ResponseEntity<ContestDetailsResponse> getContestDetails(@PathVariable String contestCode, @RequestHeader(value = "X-User-Id") Long userId) {
        try {
            if (!this.contestService.isValidContest(contestCode)) {
                return ResponseEntity.notFound().build();
            }
            ContestDetailsResponse response = this.contestService.getContestDetails(contestCode, userId);
            return ResponseEntity.ok((ContestDetailsResponse) response);
        } catch (Exception e) {
            log.error("Error getting contest details: {}", (Object) e.getMessage(), (Object) e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = {"/{contestCode}/validate"})
    public ResponseEntity<Boolean> validateContest(@PathVariable String contestCode) {
        try {
            boolean isValid = this.contestService.isValidContest(contestCode);
            return ResponseEntity.ok((Boolean) isValid);
        } catch (Exception e) {
            log.error("Error validating contest: {}", (Object) e.getMessage(), (Object) e);
            return ResponseEntity.ok((Boolean) false);
        }
    }

    @PostMapping(value = {"/code/run"})
    public ResponseEntity<CodeExecutionResponse> runCode(@RequestBody CodeExecutionRequest request) {
        try {
            log.info("Running code for problem: {}", (Object) request.getProblemName());
            if (this.codeExecutionService == null) {
                log.warn("CodeExecutionService not available - Docker not configured");
                CodeExecutionResponse response = new CodeExecutionResponse();
                response.setOverallVerdict("CE");
                response.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
                response.setError("Code execution service not available. Please ensure Docker is running and restart the service.");
                return ResponseEntity.ok((CodeExecutionResponse) response);
            }
            CodeExecutionService.ExecutionResult result = this.codeExecutionService.executeCode(request.getCode(), request.getLanguage(), request.getProblemName(), false, request.getTimeLimitMs(), request.getMemoryLimitMb());
            CodeExecutionResponse response = new CodeExecutionResponse();
            response.setOverallVerdict(result.getOverallVerdict());
            if (result.getTestResults() != null) {
                response.setTestResults(result.getTestResults().stream().map(tr -> {
                    CodeExecutionResponse.TestResult testResult = new CodeExecutionResponse.TestResult();
                    testResult.setTestNumber(tr.getTestNumber());
                    testResult.setVerdict(tr.getVerdict());
                    testResult.setTimeMs(tr.getTimeMs());
                    testResult.setOutput(tr.getOutput());
                    testResult.setError(tr.getError());
                    return testResult;
                }).toList());
            } else {
                response.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
            }
            response.setError(result.getError());
            return ResponseEntity.ok((CodeExecutionResponse) response);
        } catch (Exception e) {
            log.error("Error running code: {}", (Object) e.getMessage(), (Object) e);
            CodeExecutionResponse errorResponse = new CodeExecutionResponse();
            errorResponse.setOverallVerdict("CE");
            errorResponse.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
            errorResponse.setError(e.getMessage());
            return ResponseEntity.ok((CodeExecutionResponse) errorResponse);
        }
    }

    @PostMapping(value = {"/code/submit", "/{contestCode}/code/submit"})
    public ResponseEntity<CodeExecutionResponse> submitCode(@PathVariable(required = false) String contestCode,
                                                           @RequestBody CodeExecutionRequest request) {
        try {
            log.info("Submitting code for problem: {}", (Object) request.getProblemName());
            
            // DEBUG: Log all request details
            log.info("=== SUBMISSION REQUEST DEBUG ===");
            log.info("🎯 Contest Code (path param): '{}'", contestCode);
            log.info("📝 Request contestId: {}", request.getContestId());
            log.info("👤 Request userId: {}", request.getUserId());
            log.info("🏷️ Request userName: '{}'", request.getUserName());
            log.info("⏱️ Request timeFromStartMs: {}", request.getTimeFromStartMs());
            log.info("🧩 Request problemId: {}", request.getProblemId());
            log.info("📋 Request problemName: '{}'", request.getProblemName());
            log.info("💻 Request language: '{}'", request.getLanguage());
            log.info("🌐 Request URL pattern matched: {}", contestCode != null ? "/{contestCode}/code/submit" : "/code/submit");
            log.info("================================");
            
            // Save submission to MongoDB first
            String submissionId = null;
            log.info("SubmissionService status: {}", submissionService != null ? "Available" : "NULL");
            
            if (submissionService != null) {
                try {
                    log.info("Attempting to save submission to MongoDB...");
                    
                    // Get contest ID - try multiple sources
                    Long contestId = null;
                    Long userId = request.getUserId() != null ? request.getUserId() : 1L;
                    String userName = request.getUserName() != null ? request.getUserName() : "anonymous";
                    Long timeFromStart = request.getTimeFromStartMs() != null ? request.getTimeFromStartMs() : System.currentTimeMillis();
                    
                    // Get the REAL contest ID from the contest that was created
                    // Priority 1: Get from request body if available
                    if (request.getContestId() != null && request.getContestId() > 0) {
                        contestId = request.getContestId();
                        log.info("Using contestId from request: {}", contestId);
                    }
                    // Priority 2: Get from contest code in URL path
                    else if (contestCode != null && !contestCode.isEmpty()) {
                        try {
                            Optional<Contest> contestOpt = contestService.getContestByCode(contestCode);
                            if (contestOpt.isPresent()) {
                                contestId = contestOpt.get().getId();
                                log.info("Found REAL contestId {} for contestCode {}", contestId, contestCode);
                            } else {
                                log.error("Contest not found for code: {}", contestCode);
                                // Don't save submission if contest doesn't exist
                                log.warn("Skipping MongoDB save - invalid contest code");
                                contestId = null;
                            }
                        } catch (Exception e) {
                            log.error("Error finding contest for code {}: {}", contestCode, e.getMessage());
                            contestId = null;
                        }
                    }
                    else {
                        log.error("No contest context provided - cannot determine real contest ID");
                        contestId = null;
                    }
                    
                    // Only save if we have a valid contest ID
                    if (contestId == null) {
                        log.warn("Cannot save submission - no valid contest ID found");
                        // Skip MongoDB saving but continue with code execution
                    } else {
                    
                        var submission = submissionService.saveSubmission(
                            contestId,
                            userId,
                            userName,
                            request.getProblemId(),
                            request.getProblemName(),
                            request.getCode(),
                            request.getLanguage(),
                            timeFromStart
                        );
                        submissionId = submission.getId();
                        log.info("Successfully saved submission to MongoDB with contestId={}, submissionId={}", 
                                contestId, submissionId);
                    }
                } catch (Exception e) {
                    log.error("Failed to save submission to MongoDB: {}", e.getMessage(), e);
                }
            } else {
                log.warn("Cannot save submission - SubmissionService is NULL");
            }
            
            if (this.codeExecutionService == null) {
                log.warn("CodeExecutionService not available - Docker not configured");
                CodeExecutionResponse response = new CodeExecutionResponse();
                response.setOverallVerdict("CE");
                response.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
                response.setError("Code execution service not available. Please ensure Docker is running and restart the service.");
                return ResponseEntity.ok((CodeExecutionResponse) response);
            }
            CodeExecutionService.ExecutionResult result = this.codeExecutionService.executeCode(request.getCode(), request.getLanguage(), request.getProblemName(), true, request.getTimeLimitMs(), request.getMemoryLimitMb());
            CodeExecutionResponse response = new CodeExecutionResponse();
            response.setOverallVerdict(result.getOverallVerdict());
            if (result.getTestResults() != null) {
                response.setTestResults(result.getTestResults().stream().map(tr -> {
                    CodeExecutionResponse.TestResult testResult = new CodeExecutionResponse.TestResult();
                    testResult.setTestNumber(tr.getTestNumber());
                    testResult.setVerdict(tr.getVerdict());
                    testResult.setTimeMs(tr.getTimeMs());
                    testResult.setOutput(tr.getOutput());
                    testResult.setError(tr.getError());
                    return testResult;
                }).toList());
            } else {
                response.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
            }
            response.setError(result.getError());
            
            // Update submission with execution results
            if (submissionService != null && submissionId != null) {
                try {
                    boolean isAccepted = "AC".equals(result.getOverallVerdict());
                    int passedTests = result.getTestResults() != null ? 
                        (int) result.getTestResults().stream().filter(tr -> "AC".equals(tr.getVerdict())).count() : 0;
                    int totalTests = result.getTestResults() != null ? result.getTestResults().size() : 0;
                    
                    submissionService.updateSubmissionResults(
                        submissionId,
                        isAccepted,
                        isAccepted ? 100 : 0, // Score: 100 if accepted, 0 otherwise
                        result.getTestResults() != null && !result.getTestResults().isEmpty() ? 
                            result.getTestResults().get(0).getTimeMs() : null,
                        null, // Memory usage not available in current setup
                        result.getOverallVerdict(),
                        result.getError(),
                        passedTests,
                        totalTests
                    );
                    log.info("Updated submission {} with execution results", submissionId);
                } catch (Exception e) {
                    log.warn("Failed to update submission results: {}", e.getMessage());
                }
            }
            
            return ResponseEntity.ok((CodeExecutionResponse) response);
        } catch (Exception e) {
            log.error("Error submitting code: {}", (Object) e.getMessage(), (Object) e);
            CodeExecutionResponse errorResponse = new CodeExecutionResponse();
            errorResponse.setOverallVerdict("CE");
            errorResponse.setTestResults(new ArrayList<CodeExecutionResponse.TestResult>());
            errorResponse.setError(e.getMessage());
            return ResponseEntity.ok((CodeExecutionResponse) errorResponse);
        }

    }

    @GetMapping("/recent-rooms")
    public ResponseEntity<List<RecentRoomResponse>> getRecentRooms(@RequestHeader("X-User-Id") Long userId) {
        try {
            log.info("🏠 Dashboard: Fetching recent contest rooms for user: {}", userId);
            
            // Get top 5 recent contests for user (created + participated)
            List<RecentRoomResponse> recentRooms = getUserTop5RecentContests(userId);
            
            log.info("✅ Dashboard: Found {} recent contest rooms for user {}", recentRooms.size(), userId);
            
            // Return with browser cache headers - cache for 2 minutes for dashboard
            return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofMinutes(2)))
                .header(HttpHeaders.VARY, "X-User-Id") // Cache per user
                .body(recentRooms);
        } catch (Exception e) {
            log.error("❌ Dashboard: Error fetching recent rooms: {}", e.getMessage(), e);
            // Return empty list instead of error to prevent frontend crashes
            return ResponseEntity.ok()
                .cacheControl(CacheControl.noCache())
                .body(new ArrayList<>());
        }
    }
    
    /**
     * Get user's top 5 most recent contest rooms for dashboard
     * Returns contests created by user + contests where user participated
     */
    private List<RecentRoomResponse> getUserTop5RecentContests(Long userId) {
        try {
            log.info("🔍 Getting top 5 recent contests for user {}", userId);
            
            if (userId == null) {
                log.warn("⚠️ User ID is null, returning empty list");
                return new ArrayList<>();
            }
            
            List<RecentRoomResponse> allContests = new ArrayList<>();
            
            // Step 1: Get contests CREATED by the user (from PostgreSQL)
            try {
                List<Contest> createdContests = contestService.getContestsCreatedByUser(userId);
                log.info("📝 User {} created {} contests", userId, createdContests.size());
                
                for (Contest contest : createdContests) {
                    try {
                        if (contest == null || contest.getId() == null) {
                            log.warn("⚠️ Skipping null contest or contest with null ID");
                            continue;
                        }
                        
                        // Get total participants for this contest
                        int totalParticipants = getTotalContestParticipants(contest.getId());
                        
                        // For creator, last activity is when contest was created
                        String lastActivity = "Created contest";
                        
                        RecentRoomResponse contestResponse = RecentRoomResponse.builder()
                            .id(contest.getInviteLink())
                            .name(contest.getTitle())
                            .type("contest")
                            .participants(totalParticipants)
                            .lastActive(lastActivity)
                            .createdAt(contest.getStartTime())
                            .build();
                            
                        allContests.add(contestResponse);
                        log.info("✅ Added CREATED contest: {} (Code: {}, Participants: {})", 
                                contest.getTitle(), contest.getInviteLink(), totalParticipants);
                    } catch (Exception e) {
                        log.warn("❌ Error loading created contest metadata for ID {}: {}", contest.getId(), e.getMessage());
                    }
                }
            } catch (Exception e) {
                log.error("❌ Error fetching created contests for user {}: {}", userId, e.getMessage());
            }
            
            // Step 2: Get contests where user PARTICIPATED (has submissions in MongoDB)
            if (submissionService != null) {
                try {
                    List<Long> participatedContestIds = submissionService.getUserContestIds(userId);
                    log.info("📊 User {} participated in {} contests", userId, participatedContestIds.size());
                    
                    for (Long contestId : participatedContestIds) {
                        try {
                            if (contestId == null) {
                                log.warn("⚠️ Skipping null contest ID");
                                continue;
                            }
                            
                            // Skip if we already added this contest as a created one
                            boolean alreadyAdded = allContests.stream()
                                .anyMatch(c -> contestService.getContestById(contestId)
                                    .map(existing -> existing.getInviteLink().equals(c.getId()))
                                    .orElse(false));
                            
                            if (alreadyAdded) {
                                log.info("⏭️ Skipping contest ID {} - already added as created contest", contestId);
                                continue;
                            }
                            
                            Optional<Contest> contestOpt = contestService.getContestById(contestId);
                            if (contestOpt.isPresent()) {
                                Contest contest = contestOpt.get();
                                
                                // Get user's last activity in this contest from MongoDB
                                String lastActivity = submissionService.getLastActivityTime(userId, contestId);
                                
                                // Get total participants for this contest
                                int totalParticipants = getTotalContestParticipants(contestId);
                                
                                RecentRoomResponse contestResponse = RecentRoomResponse.builder()
                                    .id(contest.getInviteLink())
                                    .name(contest.getTitle())
                                    .type("contest")
                                    .participants(totalParticipants)
                                    .lastActive(lastActivity)
                                    .createdAt(contest.getStartTime())
                                    .build();
                                    
                                allContests.add(contestResponse);
                                log.info("✅ Added PARTICIPATED contest: {} (Code: {}, Participants: {})", 
                                        contest.getTitle(), contest.getInviteLink(), totalParticipants);
                            }
                        } catch (Exception e) {
                            log.warn("❌ Error loading participated contest metadata for ID {}: {}", contestId, e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    log.error("❌ Error fetching participated contests for user {}: {}", userId, e.getMessage());
                }
            } else {
                log.warn("⚠️ SubmissionService not available - only showing created contests");
            }
            
            // Step 3: Sort by creation date (most recent first) and limit to top 5
            List<RecentRoomResponse> top5Contests = allContests.stream()
                .filter(c -> c.getCreatedAt() != null) // Filter out contests with null dates
                .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());
            
            log.info("🎯 Dashboard: Returning {} top recent contests for user {} (out of {} total)", 
                    top5Contests.size(), userId, allContests.size());
            
            return top5Contests;
        } catch (Exception e) {
            log.error("❌ Error getting user's top 5 recent contests: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Get total number of participants for a contest
     * Counts unique users who have submitted code to this contest
     */
    private int getTotalContestParticipants(Long contestId) {
        try {
            if (contestId == null) {
                log.warn("⚠️ Contest ID is null, returning default participant count");
                return 1; // Default to 1 if contest ID is null
            }
            
            if (submissionService == null) {
                log.warn("⚠️ SubmissionService not available, returning default participant count");
                return 1; // Default to 1 if service not available
            }
            
            // Get unique user IDs who have submissions for this contest
            List<Long> participantUserIds = submissionService.getContestParticipantUserIds(contestId);
            int participantCount = participantUserIds != null ? participantUserIds.size() : 1;
            
            log.info("Contest {} has {} unique participants", contestId, participantCount);
            return participantCount;
            
        } catch (Exception e) {
            log.warn("Error getting participant count for contest {}: {}", contestId, e.getMessage());
            return 1; // Default to 1 on error
        }
    }
    
    private String calculateTimeAgo(Instant instant) {
        Instant now = Instant.now();
        Duration duration = Duration.between(instant, now);
        
        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        
        if (days > 0) {
            return days == 1 ? "1 day ago" : days + " days ago";
        } else if (hours > 0) {
            return hours == 1 ? "1 hour ago" : hours + " hours ago";
        } else if (minutes > 0) {
            return minutes == 1 ? "1 minute ago" : minutes + " minutes ago";
        } else {
            return "Just now";
        }
    }
    


    @GetMapping("/test-mongo")
    public ResponseEntity<String> testMongo() {
        try {
            log.info("=== MONGODB DEBUG TEST ===");
            log.info("SubmissionService status: {}", submissionService != null ? "Available" : "NULL");
            
            if (submissionService == null) {
                return ResponseEntity.ok("❌ SubmissionService is null - MongoDB not configured");
            }
            
            log.info("ContestSubmissionRepository status: {}", 
                submissionService.toString().contains("ContestSubmissionRepository") ? "Injected" : "Unknown");
            
            // Try to save a test submission
            log.info("Attempting to save test submission...");
            var testSubmission = submissionService.saveSubmission(
                999L, 999L, "testuser", 999L, "Test Problem", 
                "test code", "java", 0L
            );
            
            log.info("✅ Test submission saved successfully!");
            log.info("Submission ID: {}", testSubmission.getId());
            log.info("Contest ID: {}", testSubmission.getContestId());
            
            // Try to retrieve it back
            var allSubmissions = submissionService.getAllSubmissions();
            log.info("Total submissions in DB: {}", allSubmissions.size());
            
            return ResponseEntity.ok("✅ MongoDB working! Test submission ID: " + testSubmission.getId() + 
                                   ", Contest ID: " + testSubmission.getContestId() + 
                                   ", Total submissions: " + allSubmissions.size());
        } catch (Exception e) {
            log.error("❌ MongoDB test failed: {}", e.getMessage(), e);
            log.error("Full stack trace:", e);
            return ResponseEntity.ok("❌ MongoDB test failed: " + e.getMessage() + 
                                   " | Check logs for full stack trace");
        }
    }
    
    @GetMapping("/test-postgres")
    public ResponseEntity<String> testPostgres() {
        try {
            // Try to count contests
            long contestCount = contestService.getContestCount();
            return ResponseEntity.ok("PostgreSQL working! Contest count: " + contestCount);
        } catch (Exception e) {
            log.error("PostgreSQL test failed: {}", e.getMessage(), e);
            return ResponseEntity.ok("PostgreSQL test failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/fix-contest-ids")
    public ResponseEntity<String> fixContestIds(@RequestParam String contestCode) {
        try {
            if (submissionService == null) {
                return ResponseEntity.ok("SubmissionService not available");
            }
            
            // Get the real contest ID from contest code
            Optional<Contest> contestOpt = contestService.getContestByCode(contestCode);
            if (!contestOpt.isPresent()) {
                return ResponseEntity.ok("Contest not found for code: " + contestCode);
            }
            
            Long realContestId = contestOpt.get().getId();
            
            // Update all submissions with contestId=1 to use the real contest ID
            int updatedCount = submissionService.updateContestIds(1L, realContestId);
            
            return ResponseEntity.ok("Updated " + updatedCount + " submissions from contestId=1 to contestId=" + realContestId);
        } catch (Exception e) {
            log.error("Error fixing contest IDs: {}", e.getMessage(), e);
            return ResponseEntity.ok("Error fixing contest IDs: " + e.getMessage());
        }
    }
    
    @GetMapping("/{contestCode}/submissions")
    public ResponseEntity<List<ContestSubmission>> getContestSubmissions(
            @PathVariable String contestCode,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            // Get contest ID from contest code
            Optional<Contest> contestOpt = contestService.getContestByCode(contestCode);
            if (!contestOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            Long contestId = contestOpt.get().getId();
            
            // Get all submissions for this user and contest from MongoDB
            List<ContestSubmission> submissions = submissionService.getUserSubmissions(contestId, userId);
            
            log.info("Found {} submissions for user {} in contest {}", submissions.size(), userId, contestCode);
            return ResponseEntity.ok(submissions);
        } catch (Exception e) {
            log.error("Error getting contest submissions: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/available")
    public ResponseEntity<List<Contest>> getAvailableContests(@RequestHeader("X-User-Id") Long userId) {
        try {
            // Get all active contests (for discovery)
            List<Contest> availableContests = contestService.getActiveContests();
            
            // Optionally filter out contests user already participated in
            if (submissionService != null) {
                List<Long> participatedContestIds = submissionService.getUserContestIds(userId);
                availableContests = availableContests.stream()
                    .filter(contest -> !participatedContestIds.contains(contest.getId()))
                    .collect(Collectors.toList());
            }
            
            log.info("Found {} available contests for user {}", availableContests.size(), userId);
            return ResponseEntity.ok(availableContests);
        } catch (Exception e) {
            log.error("Error getting available contests: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get detailed contest room data when user clicks on a room from dashboard
     * This loads contest metadata + user's submissions for that contest
     */
    @GetMapping("/room/{contestCode}/dashboard-data")
    public ResponseEntity<Map<String, Object>> getContestRoomDashboardData(
            @PathVariable String contestCode,
            @RequestHeader("X-User-Id") Long userId) {
        try {
            log.info("🏠 Dashboard: Loading room data for contest {} and user {}", contestCode, userId);
            
            Map<String, Object> roomData = new HashMap<>();
            
            // Step 1: Get contest metadata from PostgreSQL
            Optional<Contest> contestOpt = contestService.getContestByCode(contestCode);
            if (!contestOpt.isPresent()) {
                log.warn("❌ Contest not found for code: {}", contestCode);
                return ResponseEntity.notFound().build();
            }
            
            Contest contest = contestOpt.get();
            Long contestId = contest.getId();
            
            // Step 2: Get contest basic info
            roomData.put("contestId", contestId);
            roomData.put("contestCode", contestCode);
            roomData.put("contestName", contest.getTitle());
            roomData.put("description", contest.getDescription());
            roomData.put("startTime", contest.getStartTime());
            roomData.put("endTime", contest.getEndTime());
            roomData.put("durationMinutes", contest.getDurationMinutes());
            roomData.put("problemIds", contest.getProblemIds());
            
            // Step 3: Get user's submissions for this contest from MongoDB
            if (submissionService != null) {
                List<ContestSubmission> userSubmissions = submissionService.getUserSubmissions(contestId, userId);
                roomData.put("userSubmissions", userSubmissions);
                roomData.put("totalUserSubmissions", userSubmissions.size());
                
                // Get user's accepted submissions count
                long acceptedCount = userSubmissions.stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsAccepted()))
                    .count();
                roomData.put("userAcceptedSubmissions", acceptedCount);
                
                // Get user's last activity
                String lastActivity = submissionService.getLastActivityTime(userId, contestId);
                roomData.put("userLastActivity", lastActivity);
            } else {
                roomData.put("userSubmissions", new ArrayList<>());
                roomData.put("totalUserSubmissions", 0);
                roomData.put("userAcceptedSubmissions", 0);
                roomData.put("userLastActivity", "No activity");
            }
            
            // Step 4: Get contest statistics
            int totalParticipants = getTotalContestParticipants(contestId);
            roomData.put("totalParticipants", totalParticipants);
            
            log.info("✅ Dashboard: Loaded room data for contest {} - {} submissions, {} participants", 
                    contestCode, roomData.get("totalUserSubmissions"), totalParticipants);
            
            return ResponseEntity.ok(roomData);
        } catch (Exception e) {
            log.error("❌ Dashboard: Error loading room data for contest {}: {}", contestCode, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/debug/contest-mapping")
    public ResponseEntity<Map<String, Object>> debugContestMapping() {
        try {
            Map<String, Object> debug = new HashMap<>();
            
            // Get all contests from PostgreSQL
            List<Contest> allContests = contestService.getActiveContests();
            Map<String, Long> contestCodeToId = allContests.stream()
                .collect(Collectors.toMap(Contest::getInviteLink, Contest::getId));
            
            debug.put("postgresContests", contestCodeToId);
            
            // Get submission contest IDs from MongoDB
            if (submissionService != null) {
                List<ContestSubmission> allSubmissions = submissionService.getAllSubmissions();
                Map<Long, Long> submissionCounts = allSubmissions.stream()
                    .collect(Collectors.groupingBy(
                        ContestSubmission::getContestId,
                        Collectors.counting()
                    ));
                debug.put("mongoSubmissionsByContestId", submissionCounts);
            }
            
            return ResponseEntity.ok(debug);
        } catch (Exception e) {
            log.error("Error getting debug info: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/validate-frontend-data")
    public ResponseEntity<String> validateFrontendData(@RequestBody CodeExecutionRequest request,
                                                      @RequestParam(required = false) String contestCode) {
        StringBuilder validation = new StringBuilder();
        validation.append("🔍 FRONTEND DATA VALIDATION\n");
        validation.append("===========================\n\n");
        
        boolean isValid = true;
        
        // Check required fields
        validation.append("📋 REQUIRED FIELDS:\n");
        
        if (request.getCode() == null || request.getCode().trim().isEmpty()) {
            validation.append("❌ code: MISSING or EMPTY\n");
            isValid = false;
        } else {
            validation.append("✅ code: Present (").append(request.getCode().length()).append(" chars)\n");
        }
        
        if (request.getLanguage() == null || request.getLanguage().trim().isEmpty()) {
            validation.append("❌ language: MISSING or EMPTY\n");
            isValid = false;
        } else {
            validation.append("✅ language: ").append(request.getLanguage()).append("\n");
        }
        
        if (request.getProblemId() == null) {
            validation.append("❌ problemId: MISSING\n");
            isValid = false;
        } else {
            validation.append("✅ problemId: ").append(request.getProblemId()).append("\n");
        }
        
        if (request.getProblemName() == null || request.getProblemName().trim().isEmpty()) {
            validation.append("❌ problemName: MISSING or EMPTY\n");
            isValid = false;
        } else {
            validation.append("✅ problemName: ").append(request.getProblemName()).append("\n");
        }
        
        if (request.getUserId() == null) {
            validation.append("❌ userId: MISSING - Required for MongoDB submission saving\n");
            isValid = false;
        } else {
            validation.append("✅ userId: ").append(request.getUserId()).append("\n");
        }
        
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            validation.append("❌ userName: MISSING - Required for MongoDB submission saving\n");
            isValid = false;
        } else {
            validation.append("✅ userName: ").append(request.getUserName()).append("\n");
        }
        
        if (request.getTimeFromStartMs() == null) {
            validation.append("❌ timeFromStartMs: MISSING - Required for contest timing\n");
            isValid = false;
        } else {
            validation.append("✅ timeFromStartMs: ").append(request.getTimeFromStartMs()).append(" ms\n");
        }
        
        // Check contest context
        validation.append("\n🏆 CONTEST CONTEXT:\n");
        if (contestCode != null && !contestCode.trim().isEmpty()) {
            validation.append("✅ contestCode (URL path): ").append(contestCode).append("\n");
            
            // Try to resolve contest ID
            try {
                Optional<Contest> contestOpt = contestService.getContestByCode(contestCode);
                if (contestOpt.isPresent()) {
                    validation.append("✅ Contest resolved: ID ").append(contestOpt.get().getId()).append("\n");
                } else {
                    validation.append("❌ Contest NOT FOUND for code: ").append(contestCode).append("\n");
                    isValid = false;
                }
            } catch (Exception e) {
                validation.append("❌ Error resolving contest: ").append(e.getMessage()).append("\n");
                isValid = false;
            }
        } else {
            validation.append("❌ contestCode: MISSING from URL path\n");
            isValid = false;
        }
        
        // Optional fields
        validation.append("\n⚙️ OPTIONAL FIELDS:\n");
        validation.append("timeLimitMs: ").append(request.getTimeLimitMs()).append(" ms\n");
        validation.append("memoryLimitMb: ").append(request.getMemoryLimitMb()).append(" MB\n");
        validation.append("contestId (request body): ").append(request.getContestId()).append(" (will be ignored if contestCode provided)\n");
        
        // Final validation
        validation.append("\n🎯 VALIDATION RESULT:\n");
        if (isValid) {
            validation.append("✅ ALL REQUIRED DATA PRESENT - Ready for submission!\n");
        } else {
            validation.append("❌ MISSING REQUIRED DATA - Fix frontend before submitting\n");
        }
        
        return ResponseEntity.ok(validation.toString());
    }

    @PostMapping("/test-room-code-flow")
    public ResponseEntity<String> testRoomCodeFlow(@RequestParam String roomCode) {
        try {
            log.info("=== TESTING ROOM CODE FLOW ===");
            log.info("Testing room code: {}", roomCode);
            
            // Step 1: Check if contest exists for this room code
            Optional<Contest> contestOpt = contestService.getContestByCode(roomCode);
            if (!contestOpt.isPresent()) {
                return ResponseEntity.ok("❌ Contest not found for room code: " + roomCode);
            }
            
            Contest contest = contestOpt.get();
            Long contestId = contest.getId();
            log.info("✅ Found contest ID {} for room code {}", contestId, roomCode);
            
            // Step 2: Test saving a submission with this contest ID
            if (submissionService == null) {
                return ResponseEntity.ok("❌ SubmissionService is null");
            }
            
            log.info("Attempting to save test submission with contestId {}...", contestId);
            var submission = submissionService.saveSubmission(
                contestId, // Use the REAL contest ID from PostgreSQL
                999L, // Test user ID  
                "testuser", // Test username
                1L, // Test problem ID
                "Test Problem",
                "System.out.println(\"Hello World\");", // Test code
                "java",
                System.currentTimeMillis()
            );
            
            log.info("✅ Test submission saved successfully!");
            log.info("Submission ID: {}", submission.getId());
            log.info("Contest ID in MongoDB: {}", submission.getContestId());
            
            // Step 3: Verify the submission was saved correctly
            var allSubmissions = submissionService.getAllSubmissions();
            long submissionsForThisContest = allSubmissions.stream()
                .filter(s -> contestId.equals(s.getContestId()))
                .count();
            
            return ResponseEntity.ok(String.format(
                "✅ Room Code Flow Test SUCCESSFUL!\n" +
                "Room Code: %s\n" +
                "PostgreSQL Contest ID: %d\n" +
                "MongoDB Submission ID: %s\n" +
                "MongoDB Contest ID: %d\n" +
                "Total submissions for this contest: %d\n" +
                "Total submissions in DB: %d",
                roomCode, contestId, submission.getId(), submission.getContestId(),
                submissionsForThisContest, allSubmissions.size()
            ));
            
        } catch (Exception e) {
            log.error("❌ Room code flow test failed: {}", e.getMessage(), e);
            return ResponseEntity.ok("❌ Test failed: " + e.getMessage());
        }
    }

    @PostMapping("/create-test-contest")
    public ResponseEntity<String> createTestContest() {
        try {
            log.info("=== CREATING TEST CONTEST ===");
            
            CreateContestRequest request = new CreateContestRequest();
            request.setName("Test Contest for MongoDB");
            request.setDescription("Testing room code to contest ID mapping");
            request.setDurationMinutes(60);
            request.setProblemIds(List.of(1L, 2L, 3L));
            request.setMaxParticipants(10);
            request.setIsPublic(true);
            
            RoomCreatedResponse response = contestService.createContest(request, 1L);
            
            log.info("✅ Test contest created successfully!");
            log.info("Room Code: {}", response.getInviteCode());
            log.info("Contest ID: {}", response.getRoomId());
            
            return ResponseEntity.ok(String.format(
                "✅ Test Contest Created!\n" +
                "Room Code: %s\n" +
                "Contest ID: %d\n" +
                "Now you can test submissions using this room code!",
                response.getInviteCode(), response.getRoomId()
            ));
            
        } catch (Exception e) {
            log.error("❌ Failed to create test contest: {}", e.getMessage(), e);
            return ResponseEntity.ok("❌ Failed to create test contest: " + e.getMessage());
        }
    }

    @PostMapping("/force-save-submission")
    public ResponseEntity<String> forceSaveSubmission(@RequestBody CodeExecutionRequest request) {
        try {
            log.info("=== FORCE SAVE DEBUG ===");
            log.info("SubmissionService: {}", submissionService != null ? "Available" : "NULL");
            
            if (submissionService == null) {
                return ResponseEntity.ok("❌ SubmissionService is null");
            }
            
            // Force save with default contest data
            log.info("Attempting to force save submission...");
            var submission = submissionService.saveSubmission(
                1L, // Default contest ID
                1L, // Default user ID  
                "testuser", // Default username
                request.getProblemId() != null ? request.getProblemId() : 1L,
                request.getProblemName() != null ? request.getProblemName() : "Unknown Problem",
                request.getCode(),
                request.getLanguage(),
                System.currentTimeMillis()
            );
            
            log.info("✅ Force save successful! ID: {}", submission.getId());
            
            // Verify it was saved by retrieving all submissions
            var allSubmissions = submissionService.getAllSubmissions();
            log.info("Total submissions after save: {}", allSubmissions.size());
            
            return ResponseEntity.ok("✅ Forced save successful! Submission ID: " + submission.getId() + 
                                   ", Total submissions: " + allSubmissions.size());
        } catch (Exception e) {
            log.error("❌ Force save failed: {}", e.getMessage(), e);
            log.error("Full stack trace:", e);
            return ResponseEntity.ok("❌ Force save failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/debug/submission-flow")
    public ResponseEntity<String> debugSubmissionFlow() {
        try {
            StringBuilder debug = new StringBuilder();
            debug.append("=== SUBMISSION FLOW DEBUG ===\n");
            
            // Check services
            debug.append("1. SubmissionService: ").append(submissionService != null ? "✅ Available" : "❌ NULL").append("\n");
            debug.append("2. ContestService: ").append(contestService != null ? "✅ Available" : "❌ NULL").append("\n");
            
            // Check MongoDB connection
            if (submissionService != null) {
                try {
                    var allSubmissions = submissionService.getAllSubmissions();
                    debug.append("3. MongoDB Connection: ✅ Working (").append(allSubmissions.size()).append(" submissions)\n");
                } catch (Exception e) {
                    debug.append("3. MongoDB Connection: ❌ Failed - ").append(e.getMessage()).append("\n");
                }
            }
            
            // Check PostgreSQL connection
            if (contestService != null) {
                try {
                    long contestCount = contestService.getContestCount();
                    debug.append("4. PostgreSQL Connection: ✅ Working (").append(contestCount).append(" contests)\n");
                } catch (Exception e) {
                    debug.append("4. PostgreSQL Connection: ❌ Failed - ").append(e.getMessage()).append("\n");
                }
            }
            
            return ResponseEntity.ok(debug.toString());
        } catch (Exception e) {
            log.error("Debug flow failed: {}", e.getMessage(), e);
            return ResponseEntity.ok("❌ Debug failed: " + e.getMessage());
        }
    }
    
    @PostMapping("/debug/simple-save")
    public ResponseEntity<String> simpleSave() {
        try {
            log.info("=== SIMPLE SAVE TEST ===");
            
            if (submissionService == null) {
                return ResponseEntity.ok("❌ SubmissionService is NULL");
            }
            
            // Try the simplest possible save
            var submission = submissionService.saveSubmission(
                123L,           // contestId
                456L,           // userId  
                "debuguser",    // userName
                789L,           // problemId
                "Debug Problem", // problemTitle
                "System.out.println(\"Hello World\");", // code
                "java",         // language
                System.currentTimeMillis() // timeFromStartMs
            );
            
            return ResponseEntity.ok("✅ Simple save successful! ID: " + submission.getId() + 
                                   ", ContestId: " + submission.getContestId());
        } catch (Exception e) {
            log.error("❌ Simple save failed: {}", e.getMessage(), e);
            return ResponseEntity.ok("❌ Simple save failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/debug/count-submissions")
    public ResponseEntity<String> countSubmissions() {
        try {
            if (submissionService == null) {
                return ResponseEntity.ok("❌ SubmissionService is NULL");
            }
            
            var allSubmissions = submissionService.getAllSubmissions();
            StringBuilder result = new StringBuilder();
            result.append("📊 MongoDB Configuration:\n");
            result.append("- Database: Collabdb\n");
            result.append("- Collection: contest_room_submissions\n");
            result.append("- URI: mongodb://localhost:27017/Collabdb\n\n");
            result.append("📊 Total submissions in MongoDB: ").append(allSubmissions.size()).append("\n");
            
            if (!allSubmissions.isEmpty()) {
                result.append("Recent submissions:\n");
                allSubmissions.stream()
                    .limit(5)
                    .forEach(s -> result.append("- ID: ").append(s.getId())
                        .append(", ContestId: ").append(s.getContestId())
                        .append(", UserId: ").append(s.getUserId())
                        .append(", Problem: ").append(s.getProblemTitle())
                        .append("\n"));
            } else {
                result.append("❌ No submissions found in collection 'contest_room_submissions'\n");
                result.append("💡 Try running POST /api/contests/debug/simple-save first\n");
            }
            
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            log.error("❌ Count submissions failed: {}", e.getMessage(), e);
            return ResponseEntity.ok("❌ Count failed: " + e.getMessage());
        }
    }
}
