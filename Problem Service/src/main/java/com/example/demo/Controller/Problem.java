package com.example.demo.Controller;

import com.example.demo.dto.problem.ProblemResponseDTO;
import com.example.demo.dto.problem.TopicDTO;
import com.example.demo.dto.problem.ProblemListDTO;
import com.example.demo.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/problem")
public class Problem {

    private final ProblemService problemService;
    @GetMapping("/{problemId}")
    public ResponseEntity<ProblemResponseDTO> getProblemById(
            @PathVariable Long problemId,
            @RequestParam String language // e.g., "python"
    ) {
        return problemService.getProblemById(problemId, language);
    }


//   get problems by topic id
//    get all topics ,

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<ProblemListDTO>>  getProblemByTopicId(
            @PathVariable Long topicId) {
        return problemService.getProblemByTopicId(topicId);
    }

    @GetMapping("/topicList")
    public  ResponseEntity<List<TopicDTO>> getAllTopics() {
        return problemService.getAllTopics();
    }
}
