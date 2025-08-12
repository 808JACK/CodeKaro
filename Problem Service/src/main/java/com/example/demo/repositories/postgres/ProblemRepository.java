package com.example.demo.repositories.postgres;

import com.example.demo.entities.postgres.ProblemPointer;
import com.example.demo.entities.postgres.Difficulty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<ProblemPointer, Long> {
    Page<ProblemPointer> findByDifficulty(Difficulty difficulty, Pageable pageable);
    @Query(value = "SELECT problem_id, title, difficulty, description_doc_id, topic_ids FROM problems WHERE topic_ids @> CAST(:topicId AS jsonb)", nativeQuery = true)
    List<Object[]> findByTopicIdNative(@Param("topicId") String topicId);
}