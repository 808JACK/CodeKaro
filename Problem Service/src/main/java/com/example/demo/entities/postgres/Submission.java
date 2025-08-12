package com.example.demo.entities.postgres;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "submissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submission_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "room_id")
    private Long roomId;  // Optional, can be null for direct problem submissions

    @Column(name = "language", nullable = false)
    private String language;

    @Column(name = "submitted_at", nullable = false)
    private ZonedDateTime submittedAt;

    @Column(name = "status", nullable = false)
    private String status;  // QUEUED, RUNNING, ACCEPTED, etc.

    @Column(name = "execution_time_ms")
    private Integer executionTimeMs;

    @Column(name = "memory_usage_kb")
    private Integer memoryUsageKb;

    @Column(name = "code_snapshot_doc_id", nullable = false)
    private String codeSnapshotDocId;  // References MongoDB UserSubmittedCode._id

    @Column(name = "error_message_doc_id")
    private String errorMessageDocId;  // References MongoDB ExecutionErrorLogs._id

    @Column(name = "test_case_results", columnDefinition = "jsonb")
    private String testCaseResults;  // JSON string of test case results
} 