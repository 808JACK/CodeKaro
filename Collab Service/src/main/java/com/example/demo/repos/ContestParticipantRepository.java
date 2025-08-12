/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.example.demo.repos;

import com.example.demo.entities.ContestParticipant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestParticipantRepository
extends JpaRepository<ContestParticipant, Long> {
    public Optional<ContestParticipant> findByContestIdAndUserId(Long var1, Long var2);

    public List<ContestParticipant> findByContestIdOrderByTotalScoreDescTotalTimeMsAsc(Long var1);

    @Query(value="SELECT COUNT(cp) FROM ContestParticipant cp WHERE cp.contestId = :contestId")
    public Integer countByContestId(@Param(value="contestId") Long var1);

    public boolean existsByContestIdAndUserId(Long var1, Long var2);
}
