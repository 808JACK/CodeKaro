package com.example.demo.dto.problem;

import com.example.demo.entities.postgres.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemListDTO {
    private Long id;
    private String title;
    private Difficulty difficulty;
} 