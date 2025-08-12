package com.example.demo.entities.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(collection = "problem_descriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDescription {

    @Id
    private Long problemId;

    @Field("content")
    private String content;  // Full problem statement

    @Field("constraints")
    private String constraints;

    @Field("input_format")
    private String inputFormat;

    @Field("output_format")
    private String outputFormat;

    @Field("examples")
    private List<Map<String, String>> examples;  // Sample inputs and outputs

    @Field("sample_test_cases")
    private Map<String, String> sampleTestCaseUrls;  // URLs to sample test case files

    @Field("hidden_test_cases")
    private Map<String, String> hiddenTestCaseUrls;  // URLs to hidden test case files
}