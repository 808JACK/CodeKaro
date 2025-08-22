package com.example.demo.services;

import com.example.demo.entities.ContestSubmission;
import com.example.demo.repos.ContestSubmissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmissionService {
    
    private final ContestSubmissionRepository contestSubmissionRepository;
    
    /**
     * Save a code submission to MongoDB
     */
    public ContestSubmission saveSubmission(Long contestId, Long userId, String userName, 
                                          Long problemId, String problemTitle, String code, 
                                          String language, Long timeFromStartMs) {
        try {
            log.info("=== MONGODB SAVE DEBUG ===");
            log.info("Target Database: Collabdb");
            log.info("Target Collection: contest_room_submissions");
            log.info("SubmissionService.saveSubmission called with contestId={}, userId={}, problemId={}", 
                    contestId, userId, problemId);
            log.info("Repository status: {}", contestSubmissionRepository != null ? "Available" : "NULL");
            log.info("Repository class: {}", contestSubmissionRepository != null ? contestSubmissionRepository.getClass().getName() : "NULL");
            
            if (contestSubmissionRepository == null) {
                log.error("❌ CRITICAL: ContestSubmissionRepository is NULL!");
                log.error("This indicates MongoDB configuration failed to load properly");
                log.error("Check: 1) mongodb.enabled=true, 2) MongoDB running, 3) @EnableMongoRepositories");
                throw new RuntimeException("ContestSubmissionRepository is NULL - MongoDB not properly configured");
            }
            
            // Generate sequential submission number
            log.info("Generating submission number...");
            Long submissionNumber = getNextSubmissionNumber();
            log.info("Generated submission number: {}", submissionNumber);
            
            log.info("Building ContestSubmission object...");
            ContestSubmission submission = ContestSubmission.builder()
                .submissionNumber(submissionNumber)
                .contestId(contestId)
                .userId(userId)
                .userName(userName)
                .problemId(problemId)
                .problemTitle(problemTitle)
                .code(code)
                .language(language)
                .submittedAt(Instant.now())
                .timeFromStartMs(timeFromStartMs)
                .status("SUBMITTED")
                .build();
            
            log.info("Built submission object - ContestId: {}, UserId: {}, ProblemId: {}", 
                    submission.getContestId(), submission.getUserId(), submission.getProblemId());
            log.info("Attempting to save to MongoDB collection 'contest_room_submissions'...");
            
            ContestSubmission saved = contestSubmissionRepository.save(submission);
            
            log.info("✅ Successfully saved submission to MongoDB!");
            log.info("Saved submission ID: {}", saved.getId());
            log.info("Saved contestId: {}", saved.getContestId());
            log.info("Saved to database: Collabdb, collection: contest_room_submissions");
            
            // Verify it was saved by counting total submissions
            try {
                long totalCount = contestSubmissionRepository.count();
                log.info("Total submissions in database after save: {}", totalCount);
            } catch (Exception countEx) {
                log.warn("Could not count submissions after save: {}", countEx.getMessage());
            }
            
            return saved;
        } catch (Exception e) {
            log.error("❌ Error saving submission to MongoDB: {}", e.getMessage(), e);
            log.error("Full stack trace:", e);
            throw new RuntimeException("Failed to save submission to MongoDB", e);
        }
    }
    
    /**
     * Update submission with execution results
     */
    public ContestSubmission updateSubmissionResults(String submissionId, Boolean isAccepted, 
                                                   Integer score, Long executionTimeMs, 
                                                   Integer memoryUsedKb, String status, 
                                                   String errorMessage, Integer testCasesPassed, 
                                                   Integer totalTestCases) {
        try {
            Optional<ContestSubmission> submissionOpt = contestSubmissionRepository.findById(submissionId);
            if (submissionOpt.isEmpty()) {
                throw new RuntimeException("Submission not found: " + submissionId);
            }
            
            ContestSubmission submission = submissionOpt.get();
            submission.setIsAccepted(isAccepted);
            submission.setScore(score);
            submission.setExecutionTimeMs(executionTimeMs);
            submission.setMemoryUsedKb(memoryUsedKb);
            submission.setStatus(status);
            submission.setErrorMessage(errorMessage);
            submission.setTestCasesPassed(testCasesPassed);
            submission.setTotalTestCases(totalTestCases);
            submission.setJudgedAt(Instant.now());
            
            ContestSubmission updated = contestSubmissionRepository.save(submission);
            log.info("Updated submission {} with results: {}", submissionId, status);
            return updated;
        } catch (Exception e) {
            log.error("Error updating submission results: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update submission results", e);
        }
    }
    
    /**
     * Get all submissions for a contest and user
     */
    public List<ContestSubmission> getUserSubmissions(Long contestId, Long userId) {
        try {
            return contestSubmissionRepository.findByContestIdAndUserId(contestId, userId);
        } catch (Exception e) {
            log.error("Error fetching user submissions: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user submissions", e);
        }
    }
    
    /**
     * Get user's submissions for a specific problem in a contest
     */
    public List<ContestSubmission> getUserProblemSubmissions(Long contestId, Long userId, Long problemId) {
        try {
            return contestSubmissionRepository.findByContestIdAndUserIdAndProblemId(contestId, userId, problemId);
        } catch (Exception e) {
            log.error("Error fetching user problem submissions: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user problem submissions", e);
        }
    }
    
    /**
     * Get user's accepted submission for a problem (if any)
     */
    public Optional<ContestSubmission> getUserAcceptedSubmission(Long contestId, Long userId, Long problemId) {
        try {
            return contestSubmissionRepository.findByContestIdAndUserIdAndProblemIdAndIsAcceptedTrue(contestId, userId, problemId);
        } catch (Exception e) {
            log.error("Error fetching accepted submission: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
    
    /**
     * Get all submissions for a contest (for leaderboard/admin view)
     */
    public List<ContestSubmission> getContestSubmissions(Long contestId) {
        try {
            return contestSubmissionRepository.findByContestIdOrderBySubmittedAtDesc(contestId);
        } catch (Exception e) {
            log.error("Error fetching contest submissions: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch contest submissions", e);
        }
    }
    
    /**
     * Get all submissions for a user across all contests (for recent rooms)
     */
    public List<ContestSubmission> getAllUserSubmissions(Long userId) {
        try {
            return contestSubmissionRepository.findByUserIdOrderBySubmittedAtDesc(userId);
        } catch (Exception e) {
            log.error("Error fetching all user submissions: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all user submissions", e);
        }
    }
    
    /**
     * Generate next sequential submission number
     */
    private Long getNextSubmissionNumber() {
        try {
            // Get the highest submission number and increment by 1
            List<ContestSubmission> allSubmissions = contestSubmissionRepository.findAll();
            Long maxNumber = allSubmissions.stream()
                .filter(s -> s.getSubmissionNumber() != null)
                .mapToLong(ContestSubmission::getSubmissionNumber)
                .max()
                .orElse(0L);
            return maxNumber + 1;
        } catch (Exception e) {
            log.warn("Error getting next submission number, using timestamp: {}", e.getMessage());
            // Fallback to timestamp-based number
            return System.currentTimeMillis() % 1000000; // Last 6 digits of timestamp
        }
    }
    
    /**
     * Update contest IDs for existing submissions (migration helper)
     */
    public int updateContestIds(Long oldContestId, Long newContestId) {
        try {
            List<ContestSubmission> submissions = contestSubmissionRepository.findByContestId(oldContestId);
            int updatedCount = 0;
            
            for (ContestSubmission submission : submissions) {
                submission.setContestId(newContestId);
                contestSubmissionRepository.save(submission);
                updatedCount++;
            }
            
            log.info("Updated {} submissions from contestId {} to {}", updatedCount, oldContestId, newContestId);
            return updatedCount;
        } catch (Exception e) {
            log.error("Error updating contest IDs: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update contest IDs", e);
        }
    }
    
    /**
     * Get unique contest IDs that user has participated in (pointer approach)
     */
    public List<Long> getUserContestIds(Long userId) {
        try {
            List<ContestSubmission> submissions = contestSubmissionRepository.findByUserIdOrderBySubmittedAtDesc(userId);
            
            // Get unique contest IDs ordered by most recent activity
            return submissions.stream()
                .collect(Collectors.groupingBy(
                    ContestSubmission::getContestId,
                    LinkedHashMap::new,
                    Collectors.maxBy(Comparator.comparing(ContestSubmission::getSubmittedAt))
                ))
                .entrySet().stream()
                .sorted(Map.Entry.<Long, Optional<ContestSubmission>>comparingByValue(
                    (a, b) -> b.get().getSubmittedAt().compareTo(a.get().getSubmittedAt())
                ))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting user contest IDs: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * Get unique user IDs who have participated in a specific contest
     * Used for calculating total participants in a contest
     */
    public List<Long> getContestParticipantUserIds(Long contestId) {
        try {
            List<ContestSubmission> submissions = contestSubmissionRepository.findByContestId(contestId);
            
            // Get unique user IDs from submissions
            return submissions.stream()
                .map(ContestSubmission::getUserId)
                .distinct()
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Error getting contest participant user IDs: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get last activity time for user in a specific contest
     */
    public String getLastActivityTime(Long userId, Long contestId) {
        try {
            List<ContestSubmission> submissions = contestSubmissionRepository.findByUserIdAndContestIdOrderBySubmittedAtDesc(userId, contestId);
            if (!submissions.isEmpty()) {
                Instant lastActivity = submissions.get(0).getSubmittedAt();
                return calculateTimeAgo(lastActivity);
            }
            return "No activity";
        } catch (Exception e) {
            log.warn("Error getting last activity time: {}", e.getMessage());
            return "Unknown";
        }
    }
    
    /**
     * Get user's submission count for a specific contest
     */
    public int getUserSubmissionCount(Long userId, Long contestId) {
        try {
            List<ContestSubmission> submissions = contestSubmissionRepository.findByUserIdAndContestIdOrderBySubmittedAtDesc(userId, contestId);
            return submissions.size();
        } catch (Exception e) {
            log.warn("Error getting submission count: {}", e.getMessage());
            return 0;
        }
    }
    
    /**
     * Get all submissions (for debugging)
     */
    public List<ContestSubmission> getAllSubmissions() {
        try {
            return contestSubmissionRepository.findAll();
        } catch (Exception e) {
            log.error("Error getting all submissions: {}", e.getMessage());
            return new ArrayList<>();
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
}