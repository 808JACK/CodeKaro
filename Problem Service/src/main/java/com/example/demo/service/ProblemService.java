package com.example.demo.service;

import com.example.demo.dto.problem.ProblemListDTO;
import com.example.demo.dto.problem.ProblemResponseDTO;
import com.example.demo.dto.problem.TopicDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProblemService {
    ResponseEntity<ProblemResponseDTO> getProblemById(Long problemId,String language);

    ResponseEntity<List<ProblemListDTO>>  getProblemByTopicId(Long topicId);

    ResponseEntity<List<TopicDTO>> getAllTopics();
}
