package com.example.demo.entities.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.ZonedDateTime;
import java.util.UUID;

@Document(collection = "compiler_run_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilerRunRequest {
    @Id
    private String id;

    @Field("code_content")
    private String codeContent;

    @Field("language")
    private String language;

    @Field("stdin_input")
    private String stdinInput;

    @Field("submitted_at")
    private ZonedDateTime submittedAt;

    @Field("user_id")
    private Long userId;  // Optional, can be null for anonymous runs
} 