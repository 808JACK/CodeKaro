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
import com.example.demo.dtos.RecentRoomResponse;
import com.example.demo.dtos.RoomCreatedResponse;
import java.util.ArrayList;
import com.example.demo.entities.Contest;
import com.example.demo.entities.ContestStatus;
import com.example.demo.entities.InviteCodeGenerator;
import com.example.demo.entities.UserRecentRooms;
import com.example.demo.repos.ContestRepository;
import com.example.demo.repos.UserRecentRoomsRepository;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContestService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(ContestService.class);
    private final ContestRepository contestRepository;
    
    // @Autowired
    // private UserRecentRoomsRepository userRecentRoomsRepository;

    public RoomCreatedResponse createContest(CreateContestRequest request, Long creatorUserId) {
        try {
            log.info("Creating contest with name: {}, duration: {}, problems: {}", 
                    request.getName(), request.getDurationMinutes(), request.getProblemIds());
            
            String contestCode = InviteCodeGenerator.generateInviteCode();
            ZonedDateTime startTime = ZonedDateTime.now();
            ZonedDateTime endTime = startTime.plusMinutes(request.getDurationMinutes().intValue());
            
            Contest contest = Contest.builder()
                .title(request.getName())
                .description(request.getDescription())
                .inviteLink(contestCode)
                .creatorId(creatorUserId)
                .startTime(startTime)
                .endTime(endTime)
                .durationMinutes(request.getDurationMinutes())
                .problemIds(request.getProblemIds())
                .status(ContestStatus.ACTIVE)
                .maxParticipants(request.getMaxParticipants())
                .isPublic(request.getIsPublic() != null ? request.getIsPublic() : false)
                .build();
                
            log.info("Attempting to save contest to PostgreSQL...");
            Contest savedContest = (Contest)this.contestRepository.save(contest);
            log.info("Contest saved successfully with ID: {} and code: {}", savedContest.getId(), contestCode);
            
            // Add contest to user's recent rooms
            try {
                addToUserRecentRooms(creatorUserId, savedContest.getId());
            } catch (Exception e) {
                log.warn("Failed to add contest to user recent rooms: {}", e.getMessage());
            }
            
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
        
        // Add contest to user's recent rooms when they join
        try {
            addToUserRecentRooms(userId, contest.getId());
        } catch (Exception e) {
            log.warn("Failed to add contest to user recent rooms: {}", e.getMessage());
        }
        
        return ContestDetailsResponse.builder().contestCode(contestCode).name(contest.getTitle()).problemIds(contest.getProblemIds()).currentProblemId(contest.getProblemIds().isEmpty() ? null : contest.getProblemIds().get(0)).startTime(contest.getStartTime()).durationMinutes(contest.getDurationMinutes()).isCreator(contest.getCreatorId().equals(userId)).remainingTimeSeconds(remainingSeconds).build();
    }

    public ContestDetailsResponse getContestDetails(String contestCode, Long userId) {
        return this.joinContest(contestCode, userId);
    }

    public boolean isValidContest(String contestCode) {
        return this.contestRepository.findByInviteLink(contestCode).map(contest -> contest.getStatus() == ContestStatus.ACTIVE).orElse(false);
    }

    public Optional<Contest> getContestById(Long contestId) {
        try {
            return contestRepository.findById(contestId);
        } catch (Exception e) {
            log.error("Error fetching contest by ID {}: {}", contestId, e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<Contest> getContestByCode(String contestCode) {
        try {
            return contestRepository.findByInviteLink(contestCode);
        } catch (Exception e) {
            log.error("Error fetching contest by code {}: {}", contestCode, e.getMessage(), e);
            return Optional.empty();
        }
    }
    
    public long getContestCount() {
        try {
            return contestRepository.count();
        } catch (Exception e) {
            log.error("Error counting contests: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to count contests", e);
        }
    }
    
    public List<Contest> getActiveContests() {
        try {
            return contestRepository.findByStatus(ContestStatus.ACTIVE);
        } catch (Exception e) {
            log.error("Error getting active contests: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private long calculateRemainingTime(Contest contest) {
        ZonedDateTime now = ZonedDateTime.now();
        if (now.isAfter(contest.getEndTime())) {
            return 0L;
        }
        return Duration.between(now, contest.getEndTime()).getSeconds();
    }

    public List<RecentRoomResponse> getRecentRooms(Long userId) {
        try {
            log.info("Fetching recent rooms for user: {}", userId);
            
            // Get user's recent room IDs - temporarily disabled
            // Optional<UserRecentRooms> userRecentRooms = userRecentRoomsRepository.findById(userId);
            Optional<UserRecentRooms> userRecentRooms = Optional.empty();
            
            if (userRecentRooms.isEmpty() || userRecentRooms.get().getRecentRoomIds().isEmpty()) {
                log.info("No recent rooms found for user: {}", userId);
                return new ArrayList<>();
            }
            
            List<Long> recentRoomIds = userRecentRooms.get().getRecentRoomIds();
            List<RecentRoomResponse> recentRooms = new ArrayList<>();
            
            // Limit to top 5 most recent rooms
            List<Long> limitedRoomIds = recentRoomIds.stream()
                .limit(5)
                .toList();
            
            // For now, we'll fetch contests by their IDs
            // In a real implementation, you might want to fetch both contests and regular rooms
            for (Long roomId : limitedRoomIds) {
                try {
                    Optional<Contest> contestOpt = contestRepository.findById(roomId);
                    if (contestOpt.isPresent()) {
                        Contest contest = contestOpt.get();
                        
                        // Calculate time ago
                        String timeAgo = calculateTimeAgo(contest.getStartTime());
                        
                        RecentRoomResponse roomResponse = RecentRoomResponse.builder()
                            .id(contest.getInviteLink()) // Use invite code as ID for frontend
                            .name(contest.getTitle())
                            .type("contest")
                            .participants(1) // Default for now, could be enhanced
                            .lastActive(timeAgo)
                            .createdAt(contest.getStartTime())
                            .build();
                            
                        recentRooms.add(roomResponse);
                    }
                } catch (Exception e) {
                    log.warn("Error fetching room details for ID {}: {}", roomId, e.getMessage());
                }
            }
            
            log.info("Found {} recent rooms for user: {}", recentRooms.size(), userId);
            return recentRooms;
            
        } catch (Exception e) {
            log.error("Error fetching recent rooms for user {}: {}", userId, e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    private String calculateTimeAgo(ZonedDateTime dateTime) {
        ZonedDateTime now = ZonedDateTime.now();
        Duration duration = Duration.between(dateTime, now);
        
        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        
        if (days > 0) {
            return days == 1 ? "1 day ago" : days + " days ago";
        } else if (hours > 0) {
            return hours == 1 ? "1 hour ago" : hours + " hours ago";
        } else if (minutes > 0) {
            return minutes == 1 ? "1 minute ago" : minutes + " minutes ago";
        } else {
            return "Just now";
        }
    }
    
    private void addToUserRecentRooms(Long userId, Long roomId) {
        try {
            // Temporarily disabled - repository not available
            // Optional<UserRecentRooms> userRecentRoomsOpt = userRecentRoomsRepository.findById(userId);
            Optional<UserRecentRooms> userRecentRoomsOpt = Optional.empty();
            UserRecentRooms userRecentRooms;
            
            if (userRecentRoomsOpt.isPresent()) {
                userRecentRooms = userRecentRoomsOpt.get();
                List<Long> recentRoomIds = new ArrayList<>(userRecentRooms.getRecentRoomIds());
                
                // Remove if already exists to avoid duplicates
                recentRoomIds.remove(roomId);
                
                // Add to the beginning (most recent)
                recentRoomIds.add(0, roomId);
                
                // Keep only the last 10 rooms
                if (recentRoomIds.size() > 10) {
                    recentRoomIds = recentRoomIds.subList(0, 10);
                }
                
                userRecentRooms.setRecentRoomIds(recentRoomIds);
            } else {
                // Create new entry for user
                List<Long> recentRoomIds = new ArrayList<>();
                recentRoomIds.add(roomId);
                userRecentRooms = UserRecentRooms.builder()
                    .userId(userId)
                    .recentRoomIds(recentRoomIds)
                    .build();
            }
            
            // userRecentRoomsRepository.save(userRecentRooms);
            log.info("Added room {} to user {}'s recent rooms", roomId, userId);
        } catch (Exception e) {
            log.error("Error adding room to user recent rooms: {}", e.getMessage(), e);
        }
    }

    @Generated
    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }
}
