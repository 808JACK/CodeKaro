package com.example.demo.repositories.mongo;

import com.example.demo.entities.mongo.ExecutionErrorLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExecutionErrorLogRepository extends MongoRepository<ExecutionErrorLog, String> {
    Optional<ExecutionErrorLog> findBySubmissionId(Long submissionId);
}