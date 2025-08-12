package com.example.demo.repositories.mongo;

import com.example.demo.entities.mongo.CompilerRunResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CompilerRunResultRepository extends MongoRepository<CompilerRunResult, String> {
    Optional<CompilerRunResult> findByRequestId(String requestId);
} 