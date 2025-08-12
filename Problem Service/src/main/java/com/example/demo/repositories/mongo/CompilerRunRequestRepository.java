package com.example.demo.repositories.mongo;

import com.example.demo.entities.mongo.CompilerRunRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface CompilerRunRequestRepository extends MongoRepository<CompilerRunRequest, String> {
    List<CompilerRunRequest> findByUserId(Long userId);
} 