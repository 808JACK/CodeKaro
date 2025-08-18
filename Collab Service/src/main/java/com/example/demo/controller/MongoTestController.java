package com.example.demo.controller;

import com.example.demo.entities.ContestSubmission;
import com.example.demo.repos.ContestSubmissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import org.bson.Document;

@RestController
@RequestMapping("/api/mongo-test")
@Slf4j
@CrossOrigin(origins = "*")
public class MongoTestController {

    @Autowired(required = false)
    private ContestSubmissionRepository contestSubmissionRepository;
    
    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("🏓 MongoDB Test Controller is running!");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        StringBuilder status = new StringBuilder();
        status.append("=== MONGODB STATUS ===\n");
        status.append("ContestSubmissionRepository: ").append(contestSubmissionRepository != null ? "✅ Injected" : "❌ NULL").append("\n");
        status.append("MongoTemplate: ").append(mongoTemplate != null ? "✅ Injected" : "❌ NULL").append("\n");
        
        // Check MongoDB connection
        if (mongoTemplate != null) {
            try {
                String dbName = mongoTemplate.getDb().getName();
                status.append("Connected Database: ").append(dbName).append("\n");
                
                // Test connection by pinging
                mongoTemplate.getDb().runCommand(new org.bson.Document("ping", 1));
                status.append("Connection Test: ✅ SUCCESS\n");
                
                // List collections
                var collections = mongoTemplate.getCollectionNames();
                status.append("Collections: ").append(collections).append("\n");
                
                // Check if contest_room_submissions collection exists
                boolean collectionExists = mongoTemplate.collectionExists("contest_room_submissions");
                status.append("contest_room_submissions collection exists: ").append(collectionExists ? "✅ YES" : "❌ NO").append("\n");
                
            } catch (Exception e) {
                status.append("MongoDB Connection Error: ❌ ").append(e.getMessage()).append("\n");
                log.error("MongoDB connection test failed", e);
            }
        }
        
        // Test repository if available
        if (contestSubmissionRepository != null) {
            try {
                long count = contestSubmissionRepository.count();
                status.append("Repository Test: ✅ SUCCESS (").append(count).append(" documents)\n");
            } catch (Exception e) {
                status.append("Repository Test: ❌ FAILED - ").append(e.getMessage()).append("\n");
                log.error("Repository test failed", e);
            }
        }
        
        return ResponseEntity.ok(status.toString());
    }

    @PostMapping("/direct-save")
    public ResponseEntity<String> directSave() {
        try {
            log.info("=== DIRECT MONGODB SAVE TEST ===");
            
            if (mongoTemplate == null) {
                return ResponseEntity.ok("❌ MongoTemplate is NULL");
            }
            
            // Create submission directly
            ContestSubmission submission = new ContestSubmission();
            submission.setContestId(999L);
            submission.setUserId(888L);
            submission.setUserName("directtest");
            submission.setProblemId(777L);
            submission.setProblemTitle("Direct Test Problem");
            submission.setCode("System.out.println(\"Direct test\");");
            submission.setLanguage("java");
            submission.setSubmittedAt(Instant.now());
            submission.setStatus("SUBMITTED");
            submission.setTimeFromStartMs(System.currentTimeMillis());
            
            log.info("Saving directly to collection 'contest_room_submissions'...");
            ContestSubmission saved = mongoTemplate.save(submission, "contest_room_submissions");
            
            log.info("✅ Direct save successful! ID: {}", saved.getId());
            
            // Count documents in collection
            long count = mongoTemplate.count(org.springframework.data.mongodb.core.query.Query.query(
                org.springframework.data.mongodb.core.query.Criteria.where("contestId").exists(true)
            ), "contest_room_submissions");
            
            return ResponseEntity.ok("✅ Direct save successful!\n" +
                                   "Saved ID: " + saved.getId() + "\n" +
                                   "Total documents in collection: " + count);
        } catch (Exception e) {
            log.error("❌ Direct save failed: {}", e.getMessage(), e);
            return ResponseEntity.ok("❌ Direct save failed: " + e.getMessage());
        }
    }

    @PostMapping("/repository-save")
    public ResponseEntity<String> repositorySave() {
        try {
            log.info("=== REPOSITORY SAVE TEST ===");
            
            if (contestSubmissionRepository == null) {
                return ResponseEntity.ok("❌ ContestSubmissionRepository is NULL");
            }
            
            // Create submission using builder
            ContestSubmission submission = ContestSubmission.builder()
                .contestId(888L)
                .userId(777L)
                .userName("repotest")
                .problemId(666L)
                .problemTitle("Repository Test Problem")
                .code("System.out.println(\"Repository test\");")
                .language("java")
                .submittedAt(Instant.now())
                .status("SUBMITTED")
                .timeFromStartMs(System.currentTimeMillis())
                .build();
            
            log.info("Saving via repository...");
            ContestSubmission saved = contestSubmissionRepository.save(submission);
            
            log.info("✅ Repository save successful! ID: {}", saved.getId());
            
            // Count all submissions
            long count = contestSubmissionRepository.count();
            
            return ResponseEntity.ok("✅ Repository save successful!\n" +
                                   "Saved ID: " + saved.getId() + "\n" +
                                   "Total submissions: " + count);
        } catch (Exception e) {
            log.error("❌ Repository save failed: {}", e.getMessage(), e);
            return ResponseEntity.ok("❌ Repository save failed: " + e.getMessage());
        }
    }

    @GetMapping("/list-all")
    public ResponseEntity<String> listAll() {
        try {
            if (contestSubmissionRepository == null) {
                return ResponseEntity.ok("❌ ContestSubmissionRepository is NULL");
            }
            
            List<ContestSubmission> all = contestSubmissionRepository.findAll();
            StringBuilder result = new StringBuilder();
            result.append("📊 Total submissions: ").append(all.size()).append("\n");
            
            if (!all.isEmpty()) {
                result.append("Submissions:\n");
                all.forEach(s -> result.append("- ID: ").append(s.getId())
                    .append(", ContestId: ").append(s.getContestId())
                    .append(", UserId: ").append(s.getUserId())
                    .append(", Problem: ").append(s.getProblemTitle())
                    .append("\n"));
            }
            
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            log.error("❌ List all failed: {}", e.getMessage(), e);
            return ResponseEntity.ok("❌ List failed: " + e.getMessage());
        }
    }

    @GetMapping("/diagnose")
    public ResponseEntity<String> diagnose() {
        StringBuilder diagnosis = new StringBuilder();
        diagnosis.append("🔍 MONGODB DIAGNOSIS REPORT\n");
        diagnosis.append("================================\n\n");
        
        // 1. Check Spring Boot Configuration
        diagnosis.append("1. SPRING CONFIGURATION:\n");
        try {
            String mongoEnabled = System.getProperty("mongodb.enabled", "not set");
            diagnosis.append("   mongodb.enabled property: ").append(mongoEnabled).append("\n");
        } catch (Exception e) {
            diagnosis.append("   Error reading mongodb.enabled: ").append(e.getMessage()).append("\n");
        }
        
        // 2. Check Bean Injection
        diagnosis.append("\n2. BEAN INJECTION:\n");
        diagnosis.append("   ContestSubmissionRepository: ").append(contestSubmissionRepository != null ? "✅ INJECTED" : "❌ NULL").append("\n");
        diagnosis.append("   MongoTemplate: ").append(mongoTemplate != null ? "✅ INJECTED" : "❌ NULL").append("\n");
        
        // 3. Test MongoDB Connection
        diagnosis.append("\n3. MONGODB CONNECTION:\n");
        if (mongoTemplate != null) {
            try {
                // Test basic connection
                mongoTemplate.getDb().runCommand(new Document("ping", 1));
                diagnosis.append("   Connection: ✅ SUCCESS\n");
                diagnosis.append("   Database: ").append(mongoTemplate.getDb().getName()).append("\n");
                
                // Check collections
                var collections = mongoTemplate.getCollectionNames();
                diagnosis.append("   Collections: ").append(collections.size()).append(" found\n");
                collections.forEach(col -> diagnosis.append("     - ").append(col).append("\n"));
                
            } catch (Exception e) {
                diagnosis.append("   Connection: ❌ FAILED - ").append(e.getMessage()).append("\n");
            }
        } else {
            diagnosis.append("   Connection: ❌ MongoTemplate is NULL\n");
        }
        
        // 4. Test Repository
        diagnosis.append("\n4. REPOSITORY TEST:\n");
        if (contestSubmissionRepository != null) {
            try {
                long count = contestSubmissionRepository.count();
                diagnosis.append("   Repository: ✅ WORKING (").append(count).append(" documents)\n");
            } catch (Exception e) {
                diagnosis.append("   Repository: ❌ FAILED - ").append(e.getMessage()).append("\n");
            }
        } else {
            diagnosis.append("   Repository: ❌ NULL - Check @EnableMongoRepositories configuration\n");
        }
        
        // 5. Recommendations
        diagnosis.append("\n5. RECOMMENDATIONS:\n");
        if (contestSubmissionRepository == null) {
            diagnosis.append("   ⚠️  Repository is NULL - Check if MongoConfig is being loaded\n");
            diagnosis.append("   ⚠️  Verify mongodb.enabled=true in application.properties\n");
            diagnosis.append("   ⚠️  Check if MongoDB is running on localhost:27017\n");
        }
        if (mongoTemplate == null) {
            diagnosis.append("   ⚠️  MongoTemplate is NULL - MongoDB auto-configuration failed\n");
        }
        
        return ResponseEntity.ok(diagnosis.toString());
    }
}