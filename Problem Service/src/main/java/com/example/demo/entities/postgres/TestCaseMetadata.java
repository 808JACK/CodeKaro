package com.example.demo.entities.postgres;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "test_cases_metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_case_id")
    private Long id;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_sample", nullable = false)
    private boolean isSample;

    @Column(name = "input_file_path", nullable = false)
    private String inputFilePath;

    @Column(name = "output_file_path", nullable = false)
    private String outputFilePath;
}