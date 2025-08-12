package com.example.demo.entities.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "user_submitted_code")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubmittedCode {
    @Id
    private Long submissionId;

    @Field("code_content")
    private String codeContent;
}