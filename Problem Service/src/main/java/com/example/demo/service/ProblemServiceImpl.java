package com.example.demo.service;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.dto.problem.ProblemResponseDTO;
import com.example.demo.dto.problem.TopicDTO;
import com.example.demo.entities.postgres.ProblemPointer;
import com.example.demo.entities.postgres.Topic;
import com.example.demo.repositories.mongo.CodeTemplateRepository;
import com.example.demo.repositories.mongo.ProblemDescriptionRepository;
import com.example.demo.repositories.postgres.ProblemRepository;
import com.example.demo.repositories.postgres.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.dto.problem.ProblemListDTO;

import static jakarta.ws.rs.core.Response.ok;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
    private final ProblemDescriptionRepository problemDescriptionRepository;
    private final CodeTemplateRepository codeTemplateRepository;
    private final TopicRepository topicRepository;

    @Override
    public ResponseEntity<ProblemResponseDTO> getProblemById(Long problemId, String language) {
        // 1. Fetch metadata from Postgres
        ProblemPointer problemPointer = problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem not found"));

        // 2. Fetch description from MongoDB
        var problemDescription = problemDescriptionRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Description not found"));

        // 3. Fetch code template for the requested language
        var codeTemplateOpt = codeTemplateRepository.findByProblemIdAndLanguage(problemId, language);

        // 4. Fetch all code templates for supported languages
        var allTemplates = codeTemplateRepository.findByProblemId(problemId);
        List<String> supportedLanguages = allTemplates.stream()
                .map(t -> t.getLanguage())
                .distinct()
                .toList();

        // 5. Build DTO
        ProblemResponseDTO.ProblemResponseDTOBuilder builder = ProblemResponseDTO.builder()
                .id(problemPointer.getId())
                .title(problemPointer.getTitle())
                .difficulty(problemPointer.getDifficulty())
                .topicIds(problemPointer.getTopicIds())
                .content(problemDescription.getContent())
                .constraints(problemDescription.getConstraints())
                .inputFormat(problemDescription.getInputFormat())
                .outputFormat(problemDescription.getOutputFormat())
                .examples(problemDescription.getExamples())
                .supportedLanguages(supportedLanguages);

        // Add code template info if present
        codeTemplateOpt.ifPresent(codeTemplate -> {
            builder.template(codeTemplate.getTemplate());
            builder.functionName(codeTemplate.getFunctionName());
            builder.methodSignature(codeTemplate.getMethodSignature());
        });

        return ResponseEntity.ok(builder.build());
    }

    public ResponseEntity<List<ProblemListDTO>> getProblemByTopicId(Long topicId) {
        List<Object[]> rows = problemRepository.findByTopicIdNative("[" + topicId + "]");
        if (rows.isEmpty()) {
            throw new ResourceNotFoundException("No problem found for topic id " + topicId);
        }

        List<ProblemListDTO> problems = rows.stream().map(row -> {
            Long problemId = ((Number) row[0]).longValue();
            String title = (String) row[1];
            com.example.demo.entities.postgres.Difficulty difficulty = null;
            if (row[2] != null) {
                difficulty = com.example.demo.entities.postgres.Difficulty.valueOf(row[2].toString());
            }
            return ProblemListDTO.builder()
                    .id(problemId)
                    .title(title)
                    .difficulty(difficulty)
                    .build();
        }).collect(Collectors.toList());

        return ResponseEntity.ok(problems);
    }


    @Override
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        List<TopicDTO> topicList = topics.stream()
                .map(t -> new TopicDTO(t.getId(), t.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(topicList);
    }

    public ResponseEntity<List<ProblemListDTO>> getProblemsByTopicId(Long topicId) {
        List<Object[]> rows = problemRepository.findByTopicIdNative("[" + topicId + "]");
        if (rows.isEmpty()) {
            throw new ResourceNotFoundException("No problem found for topic id " + topicId);
        }
        List<ProblemListDTO> problems = rows.stream().map(row -> {
            Long problemId = ((Number) row[0]).longValue();
            String title = (String) row[1];
            com.example.demo.entities.postgres.Difficulty difficulty = null;
            if (row[2] != null) {
                difficulty = com.example.demo.entities.postgres.Difficulty.valueOf(row[2].toString());
            }
            return ProblemListDTO.builder()
                    .id(problemId)
                    .title(title)
                    .difficulty(difficulty)
                    .build();
        }).collect(Collectors.toList());
        return ResponseEntity.ok(problems);
    }
}