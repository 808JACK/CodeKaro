/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 */
package com.example.demo.services;

import com.example.demo.dtos.ContestDetailsResponse;
import com.example.demo.dtos.CreateContestRequest;
import com.example.demo.dtos.RoomCreatedResponse;
import com.example.demo.entities.Contest;
import com.example.demo.entities.ContestStatus;
import com.example.demo.entities.InviteCodeGenerator;
import com.example.demo.repos.ContestRepository;
import java.time.Duration;
import java.time.ZonedDateTime;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ContestService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ContestService.class);
    private final ContestRepository contestRepository;

    public RoomCreatedResponse createContest(CreateContestRequest request, Long creatorUserId) {
        try {
            String contestCode = InviteCodeGenerator.generateInviteCode();
            ZonedDateTime startTime = ZonedDateTime.now();
            ZonedDateTime endTime = startTime.plusMinutes(request.getDurationMinutes().intValue());
            Contest contest = Contest.builder().title(request.getName()).description(request.getDescription()).inviteLink(contestCode).creatorId(creatorUserId).startTime(startTime).endTime(endTime).durationMinutes(request.getDurationMinutes()).problemIds(request.getProblemIds()).status(ContestStatus.ACTIVE).maxParticipants(request.getMaxParticipants()).isPublic(request.getIsPublic() != null ? request.getIsPublic() : false).build();
            Contest savedContest = (Contest)this.contestRepository.save(contest);
            log.info("Contest created successfully with code: {}", (Object)contestCode);
            return RoomCreatedResponse.builder().inviteCode(contestCode).roomId(savedContest.getId()).build();
        }
        catch (Exception e) {
            log.error("Error creating contest: {}", (Object)e.getMessage(), (Object)e);
            throw new RuntimeException("Failed to create contest", e);
        }
    }

    public ContestDetailsResponse joinContest(String contestCode, Long userId) {
        Contest contest = this.contestRepository.findByInviteLink(contestCode).orElseThrow(() -> new RuntimeException("Contest not found"));
        if (contest.getStatus() != ContestStatus.ACTIVE) {
            throw new RuntimeException("Contest is not active");
        }
        long remainingSeconds = this.calculateRemainingTime(contest);
        return ContestDetailsResponse.builder().contestCode(contestCode).name(contest.getTitle()).problemIds(contest.getProblemIds()).currentProblemId(contest.getProblemIds().isEmpty() ? null : contest.getProblemIds().get(0)).startTime(contest.getStartTime()).durationMinutes(contest.getDurationMinutes()).isCreator(contest.getCreatorId().equals(userId)).remainingTimeSeconds(remainingSeconds).build();
    }

    public ContestDetailsResponse getContestDetails(String contestCode, Long userId) {
        return this.joinContest(contestCode, userId);
    }

    public boolean isValidContest(String contestCode) {
        return this.contestRepository.findByInviteLink(contestCode).map(contest -> contest.getStatus() == ContestStatus.ACTIVE).orElse(false);
    }

    private long calculateRemainingTime(Contest contest) {
        ZonedDateTime now = ZonedDateTime.now();
        if (now.isAfter(contest.getEndTime())) {
            return 0L;
        }
        return Duration.between(now, contest.getEndTime()).getSeconds();
    }

    @Generated
    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }
}
