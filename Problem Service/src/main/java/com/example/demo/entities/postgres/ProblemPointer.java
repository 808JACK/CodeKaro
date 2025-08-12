package com.example.demo.entities.postgres;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;
import java.util.UUID;
import com.example.demo.entities.postgres.LongListJsonConverter;

@Entity
@Table(name = "problems")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemPointer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "difficulty", nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(name = "description_doc_id", nullable = false)
    private String descriptionDocId;  // References MongoDB ProblemDescription._id

    @Column(name = "topic_ids", columnDefinition = "jsonb")
    @Convert(converter = LongListJsonConverter.class)
    private List<Long> topicIds;      // Array of topic IDs stored as JSONB
} 