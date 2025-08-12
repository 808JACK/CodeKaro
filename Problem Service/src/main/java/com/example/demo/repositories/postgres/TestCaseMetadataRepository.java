package com.example.demo.repositories.postgres;

import com.example.demo.entities.postgres.TestCaseMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestCaseMetadataRepository extends JpaRepository<TestCaseMetadata, Long> {
    List<TestCaseMetadata> findByProblemId(Long problemId);
    List<TestCaseMetadata> findByProblemIdAndIsSample(Long problemId, boolean isSample);
}