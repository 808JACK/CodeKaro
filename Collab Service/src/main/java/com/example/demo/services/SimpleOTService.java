/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.stereotype.Service
 */
package com.example.demo.services;

import com.example.demo.dtos.OTOperationDTO;
import com.example.demo.entities.OTOperation;
import com.example.demo.entities.Room;
import com.example.demo.repos.RoomRepository;
import com.example.demo.services.OTServiceInterface;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name={"cassandra.enabled"}, havingValue="false", matchIfMissing=true)
public class SimpleOTService
implements OTServiceInterface {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(SimpleOTService.class);
    private final RoomRepository roomRepository;
    private final Map<String, List<OTOperation>> roomOperations = new ConcurrentHashMap<String, List<OTOperation>>();

    @Override
    public synchronized OTOperation saveOperation(OTOperationDTO operationDTO, String roomCode) {
        try {
            Room room = this.roomRepository.findByInviteCode(roomCode).orElseThrow(() -> new RuntimeException("Room not found: " + roomCode));
            OTOperation operation = OTOperation.builder().operationId(UUID.randomUUID()).roomId(room.getId()).userId(operationDTO.getUserId()).operationType(operationDTO.getOperationType()).position(operationDTO.getPosition()).chars(operationDTO.getChars()).count(operationDTO.getCount()).deleteCount(operationDTO.getDeleteCount()).version(operationDTO.getVersion()).timestamp(Instant.now()).build();
            List operations = this.roomOperations.computeIfAbsent(roomCode, k -> Collections.synchronizedList(new ArrayList()));
            operations.add(operation);
            log.debug("Saved OT operation {} for room {} by user {}", new Object[]{operation.getOperationId(), roomCode, operationDTO.getUserId()});
            return operation;
        }
        catch (Exception e) {
            log.error("Error saving OT operation for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            throw new RuntimeException("Failed to save OT operation", e);
        }
    }

    @Override
    public synchronized OTOperation processOperation(String roomCode, Long userId, OTOperationDTO operationDTO, Integer clientRevision) {
        try {
            long currentVersion = this.getOperationCount(roomCode);
            operationDTO.setVersion((int)currentVersion);
            operationDTO.setUserId(userId);
            operationDTO.setRoomCode(roomCode);
            return this.saveOperation(operationDTO, roomCode);
        }
        catch (Exception e) {
            log.error("Error processing operation for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
            throw new RuntimeException("Failed to process operation", e);
        }
    }

    @Override
    public synchronized List<OTOperation> getOperationsForRoom(String roomCode) {
        List operations = this.roomOperations.getOrDefault(roomCode, new ArrayList());
        return new ArrayList<OTOperation>(operations);
    }

    @Override
    public synchronized List<OTOperation> getOperationsAfterVersion(String roomCode, Integer version) {
        List<OTOperation> allOps = this.getOperationsForRoom(roomCode);
        return allOps.stream().filter(op -> op.getVersion() != null && op.getVersion() > version).toList();
    }

    @Override
    public synchronized long getOperationCount(String roomCode) {
        return ((List)this.roomOperations.getOrDefault(roomCode, new ArrayList())).size();
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

    @Generated
    public SimpleOTService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
}
