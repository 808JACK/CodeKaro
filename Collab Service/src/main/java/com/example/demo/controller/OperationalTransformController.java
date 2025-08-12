/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.messaging.handler.annotation.MessageMapping
 *  org.springframework.messaging.handler.annotation.Payload
 *  org.springframework.messaging.simp.SimpMessageHeaderAccessor
 *  org.springframework.messaging.simp.SimpMessagingTemplate
 *  org.springframework.stereotype.Controller
 */
package com.example.demo.controller;

import com.example.demo.dtos.OTOperationDTO;
import com.example.demo.dtos.ParticipantDto;
import com.example.demo.dtos.RoomActionDTO;
import com.example.demo.entities.OTOperation;
import com.example.demo.services.OTServiceInterface;
import com.example.demo.services.SessionRegistryService;
import java.util.HashMap;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class OperationalTransformController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(OperationalTransformController.class);
    private final OTServiceInterface otService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRegistryService sessionRegistryService;

    @MessageMapping(value={"/ot/operation"})
    public void handleOTOperation(@Payload OTOperationDTO operationDTO, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String roomCode = operationDTO.getRoomCode();
            Long userId = operationDTO.getUserId();
            if (roomCode == null || userId == null) {
                log.warn("Missing roomCode or userId in OT operation payload");
                this.sendErrorToClient(headerAccessor, "Missing required fields");
                return;
            }
            log.info("Received OT operation from user {} in room {} - Type: {}, Position: {}", new Object[]{userId, roomCode, operationDTO.getOperationType(), operationDTO.getPosition()});
            if (!this.sessionRegistryService.isUserInRoom(roomCode, userId)) {
                log.warn("User {} not in room {} - rejecting OT operation", (Object)userId, (Object)roomCode);
                this.sendErrorToClient(headerAccessor, "User not in room");
                return;
            }
            this.sessionRegistryService.updateUserActivity(roomCode, userId);
            Integer clientRevision = operationDTO.getVersion();
            OTOperation savedOperation = this.otService.processOperation(roomCode, userId, operationDTO, clientRevision);
            OTOperationDTO broadcastDTO = this.otService.convertToDTO(savedOperation, roomCode);
            HashMap<String, Object> broadcastPayload = new HashMap<String, Object>();
            broadcastPayload.put("operation", broadcastDTO);
            broadcastPayload.put("roomCode", roomCode);
            broadcastPayload.put("userId", userId);
            broadcastPayload.put("timestamp", System.currentTimeMillis());
            String destination = "/collab-topic/room/" + roomCode + "/ot-operations";
            broadcastPayload.put("senderId", userId);
            broadcastPayload.put("senderSessionId", headerAccessor.getSessionId());
            this.messagingTemplate.convertAndSend((String) destination, broadcastPayload);
            this.sendAckToClient(headerAccessor, savedOperation.getOperationId().toString());
            log.debug("Processed and broadcasted OT operation {} from user {} in room {} (client rev: {}, server rev: {})", new Object[]{savedOperation.getOperationId(), userId, roomCode, clientRevision, savedOperation.getVersion()});
        }
        catch (IllegalArgumentException e) {
            log.warn("Invalid operation from user {} in room {}: {}", new Object[]{operationDTO.getUserId(), operationDTO.getRoomCode(), e.getMessage()});
            this.sendErrorToClient(headerAccessor, "Invalid operation: " + e.getMessage());
        }
        catch (Exception e) {
            log.error("Error handling OT operation: {}", (Object)e.getMessage(), (Object)e);
            this.sendErrorToClient(headerAccessor, "Operation failed");
        }
    }

    @MessageMapping(value={"/ot/get-state"})
    public void getDocumentState(@Payload RoomActionDTO request, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String roomCode = request.getRoomCode();
            Long userId = request.getUserId();
            if (roomCode == null || userId == null) {
                log.warn("Missing roomCode or userId in state request");
                this.sendErrorToClient(headerAccessor, "Missing required fields");
                return;
            }
            log.info("Received document state request from user {} for room {}", (Object)userId, (Object)roomCode);
            if (!this.sessionRegistryService.isUserInRoom(roomCode, userId)) {
                log.warn("User {} not in room {} - rejecting state request", (Object)userId, (Object)roomCode);
                this.sendErrorToClient(headerAccessor, "User not in room");
                return;
            }
            List<ParticipantDto> participants = this.sessionRegistryService.getActiveParticipants(roomCode);
            String initialCode = "// Write your solution here\n";
            String currentCode = this.otService.reconstructCurrentCode(roomCode, initialCode);
            long currentRevision = this.otService.getOperationCount(roomCode);
            HashMap<String, Object> stateResponse = new HashMap<String, Object>();
            stateResponse.put("roomCode", roomCode);
            stateResponse.put("currentCode", currentCode);
            stateResponse.put("revision", currentRevision);
            stateResponse.put("participants", participants);
            stateResponse.put("participantCount", participants.size());
            stateResponse.put("timestamp", System.currentTimeMillis());
            String destination = "/collab-topic/room/" + roomCode + "/document-state";
            this.messagingTemplate.convertAndSend((String) destination, stateResponse);
            log.info("Sent document state for room {} - Revision: {}, Participants: {}", new Object[]{roomCode, currentRevision, participants.size()});
        }
        catch (Exception e) {
            log.error("Error getting document state: {}", (Object)e.getMessage(), (Object)e);
            this.sendErrorToClient(headerAccessor, "Failed to get document state");
        }
    }

    @MessageMapping(value={"/ot/sync"})
    public void syncOperations(@Payload RoomActionDTO syncRequest, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String roomCode = syncRequest.getRoomCode();
            Long userId = syncRequest.getUserId();
            Integer fromRevision = syncRequest.getFromVersion() != null ? syncRequest.getFromVersion() : 0;
            if (roomCode == null || userId == null) {
                log.warn("Missing roomCode or userId in sync request");
                this.sendErrorToClient(headerAccessor, "Missing required fields");
                return;
            }
            log.info("Received sync request from user {} for room {} from revision {}", new Object[]{userId, roomCode, fromRevision});
            if (!this.sessionRegistryService.isUserInRoom(roomCode, userId)) {
                log.warn("User {} not in room {} - rejecting sync request", (Object)userId, (Object)roomCode);
                this.sendErrorToClient(headerAccessor, "User not in room");
                return;
            }
            List<OTOperation> operations = this.otService.getOperationsAfterVersion(roomCode, fromRevision);
            for (OTOperation operation : operations) {
                OTOperationDTO dto = this.otService.convertToDTO(operation, roomCode);
                this.messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/collab-queue/sync-operation", (Object)dto);
            }
            HashMap<String, Object> syncComplete = new HashMap<String, Object>();
            syncComplete.put("roomCode", roomCode);
            syncComplete.put("operationCount", operations.size());
            syncComplete.put("fromRevision", fromRevision);
            syncComplete.put("currentRevision", this.otService.getOperationCount(roomCode));
            this.messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/collab-queue/sync-complete", syncComplete);
            log.info("Sent {} operations to user {} for sync in room {} (from revision {})", new Object[]{operations.size(), userId, roomCode, fromRevision});
        }
        catch (Exception e) {
            log.error("Error handling sync request: {}", (Object)e.getMessage(), (Object)e);
            this.sendErrorToClient(headerAccessor, "Sync failed");
        }
    }

    @MessageMapping(value={"/ot/ack-request"})
    public void handleAckRequest(@Payload RoomActionDTO ackRequest, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String roomCode = ackRequest.getRoomCode();
            Long userId = ackRequest.getUserId();
            if (roomCode == null || userId == null) {
                log.warn("Missing roomCode or userId in ack request");
                return;
            }
            long currentRevision = this.otService.getOperationCount(roomCode);
            HashMap<String, Object> ackResponse = new HashMap<String, Object>();
            ackResponse.put("roomCode", roomCode);
            ackResponse.put("currentRevision", currentRevision);
            ackResponse.put("timestamp", System.currentTimeMillis());
            this.messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/collab-queue/ack", ackResponse);
            log.debug("Sent ACK to user {} for room {} - Current revision: {}", new Object[]{userId, roomCode, currentRevision});
        }
        catch (Exception e) {
            log.error("Error handling ACK request: {}", (Object)e.getMessage(), (Object)e);
        }
    }

    private void sendErrorToClient(SimpMessageHeaderAccessor headerAccessor, String errorMessage) {
        HashMap<String, Object> error = new HashMap<String, Object>();
        error.put("error", errorMessage);
        error.put("timestamp", System.currentTimeMillis());
        this.messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/collab-queue/error", error);
    }

    private void sendAckToClient(SimpMessageHeaderAccessor headerAccessor, String operationId) {
        HashMap<String, Object> ack = new HashMap<String, Object>();
        ack.put("operationId", operationId);
        ack.put("status", "processed");
        ack.put("timestamp", System.currentTimeMillis());
        this.messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/collab-queue/operation-ack", ack);
    }

    @Generated
    public OperationalTransformController(OTServiceInterface otService, SimpMessagingTemplate messagingTemplate, SessionRegistryService sessionRegistryService) {
        this.otService = otService;
        this.messagingTemplate = messagingTemplate;
        this.sessionRegistryService = sessionRegistryService;
    }
}
