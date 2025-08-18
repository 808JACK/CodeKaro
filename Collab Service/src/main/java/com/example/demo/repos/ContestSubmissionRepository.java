/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.example.demo.repos;

import com.example.demo.entities.ContestSubmission;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestSubmissionRepository
extends MongoRepository<ContestSubmission, String> {
    public List<ContestSubmission> findByContestIdAndUserId(Long var1, Long var2);

    public List<ContestSubmission> findByContestIdAndUserIdAndProblemId(Long var1, Long var2, Long var3);

    public Optional<ContestSubmission> findByContestIdAndUserIdAndProblemIdAndIsAcceptedTrue(Long var1, Long var2, Long var3);

    public List<ContestSubmission> findByContestIdOrderBySubmittedAtDesc(Long var1);

    public List<ContestSubmission> findByUserIdOrderBySubmittedAtDesc(Long userId);

    public List<ContestSubmission> findByContestId(Long contestId);

    public List<ContestSubmission> findByUserIdAndContestIdOrderBySubmittedAtDesc(Long userId, Long contestId);
}
