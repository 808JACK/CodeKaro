package com.example.demo.repositories.mongo;

import com.example.demo.entities.mongo.ProblemDescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProblemDescriptionRepository extends MongoRepository<ProblemDescription, Long> {
    Optional<ProblemDescription> findByProblemId(Long problemId);
}