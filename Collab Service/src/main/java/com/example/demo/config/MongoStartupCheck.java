package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import com.example.demo.repos.ContestSubmissionRepository;

@Component
@ConditionalOnProperty(name = "mongodb.enabled", havingValue = "true")
@Slf4j
public class MongoStartupCheck implements CommandLineRunner {

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;
    
    @Autowired(required = false)
    private ContestSubmissionRepository contestSubmissionRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 MONGODB STARTUP CHECK");
        log.info("========================");
        
        // Check if MongoDB is enabled
        log.info("MongoDB enabled: true (startup check running)");
        
        // Check MongoTemplate
        if (mongoTemplate != null) {
            try {
                String dbName = mongoTemplate.getDb().getName();
                log.info("✅ MongoTemplate: Connected to database '{}'", dbName);
                
                // Test connection
                mongoTemplate.getDb().runCommand(new org.bson.Document("ping", 1));
                log.info("✅ MongoDB Connection: SUCCESS");
                
                // List collections
                var collections = mongoTemplate.getCollectionNames();
                log.info("✅ Collections found: {}", collections.size());
                
            } catch (Exception e) {
                log.error("❌ MongoDB Connection: FAILED - {}", e.getMessage());
            }
        } else {
            log.error("❌ MongoTemplate: NULL - MongoDB auto-configuration failed");
        }
        
        // Check Repository
        if (contestSubmissionRepository != null) {
            try {
                long count = contestSubmissionRepository.count();
                log.info("✅ ContestSubmissionRepository: Working ({} documents)", count);
            } catch (Exception e) {
                log.error("❌ ContestSubmissionRepository: Failed - {}", e.getMessage());
            }
        } else {
            log.error("❌ ContestSubmissionRepository: NULL - Repository not injected");
        }
        
        log.info("========================");
    }
}