package com.example.demo.entities.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.ZonedDateTime;

@Document(collection = "compiler_run_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilerRunResult {
    @Id
    private String id;

    @Field("request_id")
    @Indexed
    private String requestId;  // References CompilerRunRequest._id

    @Field("stdout")
    private String stdout;

    @Field("stderr")
    private String stderr;

    @Field("execution_error_text")
    private String executionErrorText;

    @Field("executed_at")
    private ZonedDateTime executedAt;
} 