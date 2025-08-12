package com.example.demo.repositories.mongo;

import com.example.demo.entities.mongo.UserSubmittedCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserSubmittedCodeRepository extends MongoRepository<UserSubmittedCode, Long> {
    Optional<UserSubmittedCode> findBySubmissionId(Long submissionId);
}