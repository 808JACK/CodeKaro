package com.example.demo.services;

import com.example.demo.entities.Room;
import com.example.demo.repos.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompactionService {
    private final RoomRepository roomRepository;
    private final SessionRegistryService sessionRegistryService;
    private final OTServiceInterface otService;

    public void checkForCompaction() {
        try {
            List<Room> activeRooms = this.roomRepository.findAll();
            for (Room room : activeRooms) {
                int participantCount = this.sessionRegistryService.getActiveParticipants(room.getInviteCode()).size();
                if (participantCount != 0) continue;
                log.info("Room {} has 0 participants, checking if cleanup is needed", room.getInviteCode());
                if (!this.shouldCleanupRoom(room)) continue;
                log.info("Starting cleanup for empty room {}", room.getInviteCode());
                this.cleanupEmptyRoomAsync(room);
            }
        }
        catch (Exception e) {
            log.error("Error during scheduled compaction check: {}", e.getMessage(), e);
        }
    }

    private boolean shouldCleanupRoom(Room room) {
        ZonedDateTime tenMinutesAgo = ZonedDateTime.now().minusMinutes(10L);
        if (room.getLastActiveAt() != null && room.getLastActiveAt().isBefore(tenMinutesAgo)) {
            long operationCount = this.otService.getOperationCount(room.getInviteCode());
            return operationCount > 0L;
        }
        return false;
    }

    @Async
    public CompletableFuture<Void> cleanupEmptyRoomAsync(Room room) {
        try {
            String roomCode = room.getInviteCode();
            log.info("Starting cleanup for room {}", roomCode);
            long operationCount = this.otService.getOperationCount(roomCode);
            if (operationCount > 0L) {
                String currentCode = this.otService.reconstructCurrentCode(roomCode, "// Initial code");
                log.info("Room {} cleanup completed - {} operations processed, final code length: {}", roomCode, operationCount, currentCode.length());
            } else {
                log.info("Room {} had no operations to cleanup", roomCode);
            }
        }
        catch (Exception e) {
            log.error("Error during room cleanup for {}: {}", room.getInviteCode(), e.getMessage(), e);
        }
        return CompletableFuture.completedFuture(null);
    }

    public void compactRoomManually(String roomCode) {
        try {
            Room room = this.roomRepository.findByInviteCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));
            log.info("Manual compaction requested for room {}", roomCode);
            long operationCount = this.otService.getOperationCount(roomCode);
            String currentCode = this.otService.reconstructCurrentCode(roomCode, "// Initial code");
            log.info("Manual compaction for room {} - {} operations, code length: {}", roomCode, operationCount, currentCode.length());
        }
        catch (Exception e) {
            log.error("Error during manual compaction for room {}: {}", roomCode, e.getMessage(), e);
            throw new RuntimeException("Manual compaction failed", e);
        }
    }

    public CompactionStats getCompactionStats() {
        try {
            long totalRooms = this.roomRepository.count();
            long activeRooms = this.roomRepository.findAll().stream().mapToLong(room -> this.sessionRegistryService.getActiveParticipants(room.getInviteCode()).size()).sum();
            return CompactionStats.builder().totalRooms(totalRooms).activeRooms(activeRooms).emptyRooms(totalRooms - (activeRooms > 0L ? 1 : 0)).build();
        }
        catch (Exception e) {
            log.error("Error getting compaction stats: {}", e.getMessage(), e);
            return CompactionStats.builder().totalRooms(0L).activeRooms(0L).emptyRooms(0L).build();
        }
    }

    @lombok.Data
    @lombok.Builder
    public static class CompactionStats {
        private long totalRooms;
        private long activeRooms;
        private long emptyRooms;
    }
}

