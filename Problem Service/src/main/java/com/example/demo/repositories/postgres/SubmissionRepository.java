package com.example.demo.repositories.postgres;

import com.example.demo.entities.postgres.Submission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Page<Submission> findByUserIdOrderBySubmittedAtDesc(Long userId, Pageable pageable);
    Page<Submission> findByProblemIdOrderBySubmittedAtDesc(Long problemId, Pageable pageable);
    List<Submission> findByRoomId(Long roomId);
    List<Submission> findByStatus(String status);
} 