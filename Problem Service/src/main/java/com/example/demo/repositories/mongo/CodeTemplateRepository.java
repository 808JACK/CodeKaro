package com.example.demo.repositories.mongo;

import com.example.demo.entities.mongo.CodeTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CodeTemplateRepository extends MongoRepository<CodeTemplate, String> {
    List<CodeTemplate> findByProblemId(Long problemId);
    Optional<CodeTemplate> findByProblemIdAndLanguage(Long problemId, String language);
}