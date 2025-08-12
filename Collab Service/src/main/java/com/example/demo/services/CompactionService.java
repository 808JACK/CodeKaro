/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.scheduling.annotation.Async
 *  org.springframework.stereotype.Service
 */
package com.example.demo.services;

import com.example.demo.entities.Room;
import com.example.demo.repos.RoomRepository;
import com.example.demo.services.OTServiceInterface;
import com.example.demo.services.SessionRegistryService;
import java.time.ZonedDateTime;
import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import lombok.Generated;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CompactionService {
//    @Generated
//    private static final Logger log = LoggerFactory.getLogger(CompactionService.class);
//    private final RoomRepository roomRepository;
//    private final SessionRegistryService sessionRegistryService;
//    private final OTServiceInterface otService

//    public void checkForCompaction() {
//        try {
//            List activeRooms = this.roomRepository.findAll();
//            for (Object room : activeRooms) {
//                int participantCount = this.sessionRegistryService.getActiveParticipants(room.getInviteCode()).size();
//                if (participantCount != 0) continue;
//                log.info("Room {} has 0 participants, checking if cleanup is needed", room.getInviteCode());
//                if (!this.shouldCleanupRoom(room)) continue;
//                log.info("Starting cleanup for empty room {}", room.getInviteCode());
//                this.cleanupEmptyRoomAsync(room);
//            }
//        }
//        catch (Exception e) {
//            log.error("Error during scheduled compaction check: {}", (Object)e.getMessage(), (Object)e);
//        }
//    }
//
//    private boolean shouldCleanupRoom(Room room) {
//        ZonedDateTime tenMinutesAgo = ZonedDateTime.now().minusMinutes(10L);
//        if (room.getLastActiveAt() != null && room.getLastActiveAt().isBefore(tenMinutesAgo)) {
//            long operationCount = this.otService.getOperationCount(room.getInviteCode());
//            return operationCount > 0L;
//        }
//        return false;
//    }
//
//    @Async
//    public CompletableFuture<Void> cleanupEmptyRoomAsync(Room room) {
//        try {
//            String roomCode = room.getInviteCode();
//            log.info("Starting cleanup for room {}", roomCode);
//            long operationCount = this.otService.getOperationCount(roomCode);
//            if (operationCount > 0L) {
//                String currentCode = this.otService.reconstructCurrentCode(roomCode, "// Initial code");
//                log.info("Room {} cleanup completed - {} operations processed, final code length: {}", new Object[]{roomCode, operationCount, currentCode.length()});
//            } else {
//                log.info("Room {} had no operations to cleanup", roomCode);
//            }
//        }
//        catch (Exception e) {
//            log.error("Error during room cleanup for {}: {}", new Object[]{room.getInviteCode(), e.getMessage(), e});
//        }
//        return CompletableFuture.completedFuture(null);
//    }
//
//    public void compactRoomManually(String roomCode) {
//        try {
//            Room room = this.roomRepository.findByInviteCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));
//            log.info("Manual compaction requested for room {}", roomCode);
//            long operationCount = this.otService.getOperationCount(roomCode);
//            String currentCode = this.otService.reconstructCurrentCode(roomCode, "// Initial code");
//            log.info("Manual compaction for room {} - {} operations, code length: {}", new Object[]{roomCode, operationCount, currentCode.length()});
//        }
//        catch (Exception e) {
//            log.error("Error during manual compaction for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
//            throw new RuntimeException("Manual compaction failed", e);
//        }
//    }
//
//    public CompactionStats getCompactionStats() {
//        try {
//            long totalRooms = this.roomRepository.count();
//            long activeRooms = this.roomRepository.findAll().stream().mapToLong(room -> this.sessionRegistryService.getActiveParticipants(room.getInviteCode()).size()).sum();
//            return CompactionStats.builder().totalRooms(totalRooms).activeRooms(activeRooms).emptyRooms(totalRooms - (long)(activeRooms > 0L ? 1 : 0)).build();
//        }
//        catch (Exception e) {
//            log.error("Error getting compaction stats: {}", (Object)e.getMessage(), (Object)e);
//            return CompactionStats.builder().totalRooms(0L).activeRooms(0L).emptyRooms(0L).build();
//        }
//    }
//
//    @Generated
//    public CompactionService(RoomRepository roomRepository, SessionRegistryService sessionRegistryService, OTServiceInterface otService) {
//        this.roomRepository = roomRepository;
//        this.sessionRegistryService = sessionRegistryService;
//        this.otService = otService;
//    }
//
//    public static class CompactionStats {
//        private long totalRooms;
//        private long activeRooms;
//        private long emptyRooms;
//
//        @Generated
//        public static CompactionStatsBuilder builder() {
//            return new CompactionStatsBuilder();
//        }
//
//        @Generated
//        public long getTotalRooms() {
//            return this.totalRooms;
//        }
//
//        @Generated
//        public long getActiveRooms() {
//            return this.activeRooms;
//        }
//
//        @Generated
//        public long getEmptyRooms() {
//            return this.emptyRooms;
//        }
//
//        @Generated
//        public void setTotalRooms(long totalRooms) {
//            this.totalRooms = totalRooms;
//        }
//
//        @Generated
//        public void setActiveRooms(long activeRooms) {
//            this.activeRooms = activeRooms;
//        }
//
//        @Generated
//        public void setEmptyRooms(long emptyRooms) {
//            this.emptyRooms = emptyRooms;
//        }
//
//        @Generated
//        public boolean equals(Object o) {
//            if (o == this) {
//                return true;
//            }
//            if (!(o instanceof CompactionStats)) {
//                return false;
//            }
//            CompactionStats other = (CompactionStats)o;
//            if (!other.canEqual(this)) {
//                return false;
//            }
//            if (this.getTotalRooms() != other.getTotalRooms()) {
//                return false;
//            }
//            if (this.getActiveRooms() != other.getActiveRooms()) {
//                return false;
//            }
//            return this.getEmptyRooms() == other.getEmptyRooms();
//        }
//
//        @Generated
//        protected boolean canEqual(Object other) {
//            return other instanceof CompactionStats;
//        }
//
//        @Generated
//        public int hashCode() {
//            int PRIME = 59;
//            int result = 1;
//            long $totalRooms = this.getTotalRooms();
//            result = result * 59 + (int)($totalRooms >>> 32 ^ $totalRooms);
//            long $activeRooms = this.getActiveRooms();
//            result = result * 59 + (int)($activeRooms >>> 32 ^ $activeRooms);
//            long $emptyRooms = this.getEmptyRooms();
//            result = result * 59 + (int)($emptyRooms >>> 32 ^ $emptyRooms);
//            return result;
//        }
//
//        @Generated
//        public String toString() {
//            return "CompactionService.CompactionStats(totalRooms=" + this.getTotalRooms() + ", activeRooms=" + this.getActiveRooms() + ", emptyRooms=" + this.getEmptyRooms() + ")";
//        }
//
//        @Generated
//        public CompactionStats() {
//        }
//
//        @Generated
//        public CompactionStats(long totalRooms, long activeRooms, long emptyRooms) {
//            this.totalRooms = totalRooms;
//            this.activeRooms = activeRooms;
//            this.emptyRooms = emptyRooms;
//        }
//
//        @Generated
//        public static class CompactionStatsBuilder {
//            @Generated
//            private long totalRooms;
//            @Generated
//            private long activeRooms;
//            @Generated
//            private long emptyRooms;
//
//            @Generated
//            CompactionStatsBuilder() {
//            }
//
//            @Generated
//            public CompactionStatsBuilder totalRooms(long totalRooms) {
//                this.totalRooms = totalRooms;
//                return this;
//            }
//
//            @Generated
//            public CompactionStatsBuilder activeRooms(long activeRooms) {
//                this.activeRooms = activeRooms;
//                return this;
//            }
//
//            @Generated
//            public CompactionStatsBuilder emptyRooms(long emptyRooms) {
//                this.emptyRooms = emptyRooms;
//                return this;
//            }
//
//            @Generated
//            public CompactionStats build() {
//                return new CompactionStats(this.totalRooms, this.activeRooms, this.emptyRooms);
//            }
//
//            @Generated
//            public String toString() {
//                return "CompactionService.CompactionStats.CompactionStatsBuilder(totalRooms=" + this.totalRooms + ", activeRooms=" + this.activeRooms + ", emptyRooms=" + this.emptyRooms + ")";
//            }
//        }
//    }
//}
