package com.example.demo.entities.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "execution_error_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionErrorLog {
    @Id
    private Long submissionId;

    @Field("error_text")
    private String errorText;
}