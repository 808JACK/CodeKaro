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

import com.example.demo.dtos.ParticipantDto;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomStatus;
import com.example.demo.repos.RoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionRegistryService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(SessionRegistryService.class);
    private final Map<String, Map<Long, ParticipantDto>> roomParticipants = new ConcurrentHashMap<String, Map<Long, ParticipantDto>>();
    private final Map<Long, String> userToRoom = new ConcurrentHashMap<Long, String>();
    
    @Autowired
    private RoomRepository roomRepository;

    public void userJoined(String roomCode, Long userId, String userName, String userColor) {
        try {
            long currentTime = System.currentTimeMillis();
            ParticipantDto participant = ParticipantDto.builder().userId(userId).userName(userName).userColor(userColor).joinedAt(currentTime).lastActiveAt(currentTime).isActive(true).build();
            this.roomParticipants.computeIfAbsent(roomCode, k -> new ConcurrentHashMap()).put(userId, participant);
            this.userToRoom.put(userId, roomCode);
            
            // Increment participant count in database
            incrementRoomParticipantCount(roomCode);
            
            log.info("User {} joined room {}", (Object)userId, (Object)roomCode);
        }
        catch (Exception e) {
            log.error("Error adding user {} to room {}: {}", new Object[]{userId, roomCode, e.getMessage(), e});
            throw new RuntimeException("Failed to register user session", e);
        }
    }

    public void userLeft(String roomCode, Long userId) {
        try {
            Map<Long, ParticipantDto> participants = this.roomParticipants.get(roomCode);
            if (participants != null) {
                participants.remove(userId);
                
                // Decrement participant count in database
                decrementRoomParticipantCount(roomCode);
                
                if (participants.isEmpty()) {
                    this.roomParticipants.remove(roomCode);
                    // Room is now empty, mark it as archived
                    markRoomAsArchived(roomCode);
                }
            }
            this.userToRoom.remove(userId);
            log.info("User {} left room {}", (Object)userId, (Object)roomCode);
        }
        catch (Exception e) {
            log.error("Error removing user {} from room {}: {}", new Object[]{userId, roomCode, e.getMessage(), e});
        }
    }

    public List<ParticipantDto> getActiveParticipants(String roomCode) {
        try {
            Map<Long, ParticipantDto> participants = this.roomParticipants.get(roomCode);
            if (participants == null) {
                return new ArrayList<ParticipantDto>();
            }
            return new ArrayList<ParticipantDto>(participants.values());
        }
        catch (Exception e) {
            log.error("Error getting participants for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            return new ArrayList<ParticipantDto>();
        }
    }

    public boolean isUserInRoom(String roomCode, Long userId) {
        try {
            Map<Long, ParticipantDto> participants = this.roomParticipants.get(roomCode);
            return participants != null && participants.containsKey(userId);
        }
        catch (Exception e) {
            log.error("Error checking if user {} is in room {}: {}", new Object[]{userId, roomCode, e.getMessage(), e});
            return false;
        }
    }

    public String getUserCurrentRoom(Long userId) {
        try {
            return this.userToRoom.get(userId);
        }
        catch (Exception e) {
            log.error("Error getting current room for user {}: {}", new Object[]{userId, e.getMessage(), e});
            return null;
        }
    }

    public void updateUserActivity(String roomCode, Long userId) {
        try {
            ParticipantDto participant;
            Map<Long, ParticipantDto> participants = this.roomParticipants.get(roomCode);
            if (participants != null && (participant = participants.get(userId)) != null) {
                participant.setLastActiveAt(System.currentTimeMillis());
            }
        }
        catch (Exception e) {
            log.error("Error updating activity for user {} in room {}: {}", new Object[]{userId, roomCode, e.getMessage(), e});
        }
    }

    public int getParticipantCount(String roomCode) {
        try {
            Map<Long, ParticipantDto> participants = this.roomParticipants.get(roomCode);
            return participants != null ? participants.size() : 0;
        }
        catch (Exception e) {
            log.error("Error getting participant count for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            return 0;
        }
    }

    public void clearAllSessions() {
        try {
            this.roomParticipants.clear();
            this.userToRoom.clear();
            log.info("Cleared all session data");
        }
        catch (Exception e) {
            log.error("Error clearing all sessions: {}", (Object)e.getMessage(), (Object)e);
        }
    }

    private void incrementRoomParticipantCount(String roomCode) {
        try {
            Optional<Room> roomOpt = roomRepository.findByInviteCode(roomCode);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setParticipantCount(room.getParticipantCount() + 1);
                roomRepository.save(room);
                log.debug("Incremented participant count for room {} to {}", roomCode, room.getParticipantCount());
            }
        } catch (Exception e) {
            log.error("Error incrementing participant count for room {}: {}", roomCode, e.getMessage(), e);
        }
    }

    private void decrementRoomParticipantCount(String roomCode) {
        try {
            Optional<Room> roomOpt = roomRepository.findByInviteCode(roomCode);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                int newCount = Math.max(0, room.getParticipantCount() - 1);
                room.setParticipantCount(newCount);
                
                if (newCount == 0) {
                    room.setStatus(RoomStatus.ARCHIVED);
                    log.info("Room {} marked as archived - participant count reached 0", roomCode);
                }
                
                roomRepository.save(room);
                log.debug("Decremented participant count for room {} to {}", roomCode, newCount);
            }
        } catch (Exception e) {
            log.error("Error decrementing participant count for room {}: {}", roomCode, e.getMessage(), e);
        }
    }

    private void markRoomAsArchived(String roomCode) {
        try {
            Optional<Room> roomOpt = roomRepository.findByInviteCode(roomCode);
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setStatus(RoomStatus.ARCHIVED);
                room.setParticipantCount(0);
                roomRepository.save(room);
                log.info("Room {} marked as archived - all participants left", roomCode);
            }
        } catch (Exception e) {
            log.error("Error marking room {} as archived: {}", roomCode, e.getMessage(), e);
        }
    }

    @Generated
    public SessionRegistryService() {
    }
}
