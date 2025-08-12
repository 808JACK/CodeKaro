/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.stereotype.Service
 */
package com.example.demo.services;

import com.example.demo.dtos.OTOperationDTO;
import com.example.demo.entities.OTOperation;
import com.example.demo.entities.Room;
import com.example.demo.repos.OTOperationRepository;
import com.example.demo.repos.RoomRepository;
import com.example.demo.services.OTServiceInterface;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name={"cassandra.enabled"}, havingValue="true", matchIfMissing=false)
public class OTService
implements OTServiceInterface {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(OTService.class);
    private final RoomRepository roomRepository;
    private final OTOperationRepository otOperationRepository;

    public OTService(RoomRepository roomRepository, @Autowired(required=false) OTOperationRepository otOperationRepository) {
        this.roomRepository = roomRepository;
        this.otOperationRepository = otOperationRepository;
    }

    @Override
    public OTOperation saveOperation(OTOperationDTO operationDTO, String roomCode) {
        try {
            Room room = this.roomRepository.findByInviteCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));
            OTOperation operation = OTOperation.builder().operationId(UUID.randomUUID()).roomId(room.getId()).userId(operationDTO.getUserId()).operationType(operationDTO.getOperationType()).position(operationDTO.getPosition()).chars(operationDTO.getChars()).count(operationDTO.getCount()).deleteCount(operationDTO.getDeleteCount()).version(operationDTO.getVersion()).timestamp(Instant.now()).build();
            if (this.otOperationRepository != null) {
                OTOperation savedOperation = this.otOperationRepository.save(operation);
                log.debug("Saved OT operation {} for room {} by user {}", new Object[]{savedOperation.getOperationId(), roomCode, operationDTO.getUserId()});
                return savedOperation;
            }
            log.debug("OT repository not available, returning operation without persistence");
            return operation;
        }
        catch (Exception e) {
            log.error("Error saving OT operation for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            throw new RuntimeException("Failed to save OT operation", e);
        }
    }

    @Override
    public OTOperation processOperation(String roomCode, Long userId, OTOperationDTO operationDTO, Integer clientRevision) {
        try {
            List<OTOperation> allOps;
            Room room = this.roomRepository.findByInviteCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));
            int serverRevision = this.otOperationRepository != null ? Math.toIntExact(this.otOperationRepository.countByRoomId(room.getId())) : 0;
            log.info("Processing operation for room {} - Client revision: {}, Server revision: {}", new Object[]{roomCode, clientRevision, serverRevision});
            if (clientRevision == null || clientRevision < 0 || clientRevision > serverRevision) {
                throw new IllegalArgumentException(String.format("Invalid client revision: %d. Server revision is: %d", clientRevision, serverRevision));
            }
            List<OTOperation> concurrentOps = new ArrayList<OTOperation>();
            if (clientRevision < serverRevision && this.otOperationRepository != null && (allOps = this.otOperationRepository.findByRoomIdOrderByOperationId(room.getId())).size() > clientRevision) {
                concurrentOps = allOps.subList(clientRevision, Math.min(serverRevision, allOps.size()));
            }
            OTOperationDTO transformedOpDTO = this.transformOperation(operationDTO, concurrentOps);
            OTOperation operation = OTOperation.builder().operationId(UUID.randomUUID()).roomId(room.getId()).userId(userId).operationType(transformedOpDTO.getOperationType()).position(transformedOpDTO.getPosition()).chars(transformedOpDTO.getChars()).count(transformedOpDTO.getCount()).deleteCount(transformedOpDTO.getDeleteCount()).version(serverRevision).timestamp(Instant.now()).build();
            if (this.otOperationRepository != null) {
                OTOperation savedOperation = this.otOperationRepository.save(operation);
                log.debug("Saved transformed operation {} for room {} by user {}", new Object[]{savedOperation.getOperationId(), roomCode, userId});
                return savedOperation;
            }
            log.debug("OT repository not available, returning operation without persistence");
            return operation;
        }
        catch (Exception e) {
            log.error("Error processing operation for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            throw new RuntimeException("Failed to process operation", e);
        }
    }

    @Override
    public List<OTOperation> getOperationsForRoom(String roomCode) {
        try {
            Room room = this.roomRepository.findByInviteCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));
            if (this.otOperationRepository != null) {
                return this.otOperationRepository.findByRoomIdOrderByOperationId(room.getId());
            }
            return new ArrayList<OTOperation>();
        }
        catch (Exception e) {
            log.error("Error getting operations for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            throw new RuntimeException("Failed to get operations", e);
        }
    }

    @Override
    public List<OTOperation> getOperationsAfterVersion(String roomCode, Integer version) {
        try {
            Room room = this.roomRepository.findByInviteCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));
            if (this.otOperationRepository != null) {
                return this.otOperationRepository.findByRoomIdAndVersionGreaterThan(room.getId(), version);
            }
            return new ArrayList<OTOperation>();
        }
        catch (Exception e) {
            log.error("Error getting operations after version {} for room {}: {}", new Object[]{version, roomCode, e.getMessage(), e});
            throw new RuntimeException("Failed to get operations", e);
        }
    }

    @Override
    public long getOperationCount(String roomCode) {
        try {
            Room room = this.roomRepository.findByInviteCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));
            if (this.otOperationRepository != null) {
                return this.otOperationRepository.countByRoomId(room.getId());
            }
            return 0L;
        }
        catch (Exception e) {
            log.error("Error getting operation count for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            return 0L;
        }
    }

    @Override
    public String reconstructCurrentCode(String roomCode, String initialCode) {
        try {
            List<OTOperation> operations = this.getOperationsForRoom(roomCode);
            String currentCode = initialCode != null ? initialCode : "";
            for (OTOperation operation : operations) {
                currentCode = this.applyOperationToString(currentCode, operation);
            }
            return currentCode;
        }
        catch (Exception e) {
            log.error("Error reconstructing code for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            return initialCode != null ? initialCode : "";
        }
    }

    private OTOperationDTO transformOperation(OTOperationDTO operation, List<OTOperation> concurrentOps) {
        return operation;
    }

    private String applyOperationToString(String code, OTOperation operation) {
        try {
            StringBuilder sb = new StringBuilder(code);
            switch (operation.getOperationType().toLowerCase()) {
                case "insert": {
                    if (operation.getPosition() == null || operation.getChars() == null) break;
                    int pos = Math.min(operation.getPosition(), sb.length());
                    sb.insert(pos, operation.getChars());
                    break;
                }
                case "delete": {
                    if (operation.getPosition() == null || operation.getCount() == null) break;
                    int pos = Math.min(operation.getPosition(), sb.length());
                    int endPos = Math.min(pos + operation.getCount(), sb.length());
                    sb.delete(pos, endPos);
                    break;
                }
                case "replace": {
                    if (operation.getPosition() == null || operation.getDeleteCount() == null || operation.getChars() == null) break;
                    int pos = Math.min(operation.getPosition(), sb.length());
                    int endPos = Math.min(pos + operation.getDeleteCount(), sb.length());
                    sb.delete(pos, endPos);
                    sb.insert(pos, operation.getChars());
                    break;
                }
            }
            return sb.toString();
        }
        catch (Exception e) {
            log.warn("Error applying operation {}: {}", (Object)operation.getOperationId(), (Object)e.getMessage());
            return code;
        }
    }

    @Override
    public OTOperationDTO convertToDTO(OTOperation operation, String roomCode) {
        return OTOperationDTO.builder().operationId(operation.getOperationId()).userId(operation.getUserId()).roomCode(roomCode).operationType(operation.getOperationType()).position(operation.getPosition()).chars(operation.getChars()).count(operation.getCount()).deleteCount(operation.getDeleteCount()).version(operation.getVersion()).timestamp(operation.getTimestamp().toEpochMilli()).build();
    }
}
