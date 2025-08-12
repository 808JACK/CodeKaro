package com.example.demo.entities.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;

@Document(collection = "code_templates")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(def = "{'problem_id': 1, 'language': 1}", unique = true)
public class CodeTemplate {
    @Field("problem_id")
    @Indexed
    private Long problemId;

    @Field("language")
    private String language;

    @Field("template")
    private String template;

    @Field("function_name")
    private String functionName;

    @Field("method_signature")
    private String methodSignature;

    @Field("return_type")
    private String returnType;

    @Field("parameter_types")
    private String[] parameterTypes;

    @Field("parameter_names")
    private String[] parameterNames;
}