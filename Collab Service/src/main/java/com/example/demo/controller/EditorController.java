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

import com.example.demo.dtos.CodeStateDTO;
import com.example.demo.dtos.CodeUpdateDTO;
import com.example.demo.dtos.CursorPositionDTO;
import com.example.demo.dtos.JoinRoomRequestDTO;
import com.example.demo.dtos.ParticipantDto;
import com.example.demo.dtos.ParticipantEventDTO;
import com.example.demo.dtos.RoomDetailsResponse;
import com.example.demo.dtos.SelectionInfoDTO;
import com.example.demo.services.OTServiceInterface;
import com.example.demo.services.SessionRegistryService;
import com.example.demo.services.WebSocketSessionManager;
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
public class EditorController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(EditorController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRegistryService sessionRegistryService;
    private final OTServiceInterface otService;
    private final WebSocketSessionManager webSocketSessionManager;

//    @MessageMapping(value={"/room/join"})
//    public void handleJoinRoom(@Payload JoinRoomRequestDTO joinRequest, SimpMessageHeaderAccessor headerAccessor) {
//        try {
//            String roomCode = joinRequest.getInviteCode();
//            Long userId = joinRequest.getUserId();
//            log.info("User {} attempting to join room {}", (Object)userId, (Object)roomCode);
//            if (roomCode == null || userId == null) {
//                log.warn("Invalid join request - missing roomCode or userId");
//                return;
//            }
//            String hexColor = Integer.toHexString(userId.hashCode());
//            String userColor = "#" + String.format("%06x", userId.hashCode() & 0xFFFFFF);
//            this.sessionRegistryService.userJoined(roomCode, userId, "User" + userId, userColor);
//            this.webSocketSessionManager.addSessionToRoom(roomCode, headerAccessor.getSessionId(), userId);
//            ParticipantEventDTO joinEvent = ParticipantEventDTO.builder().eventType("USER_JOINED").userId(userId).roomCode(roomCode).timestamp(System.currentTimeMillis()).build();
//            this.messagingTemplate.convertAndSend((Object)("/collab-topic/room/" + roomCode + "/participants"), (Object)joinEvent);
//            this.broadcastRoomState(roomCode);
//            this.sendCurrentCodeState(roomCode, userId);
//            log.info("User {} successfully joined room {}", (Object)userId, (Object)roomCode);
//        }
//        catch (Exception e) {
//            log.error("Error processing join request: {}", (Object)e.getMessage(), (Object)e);
//        }
//    }

//    @MessageMapping(value={"/room/cursor"})
//    public void handleCursorUpdate(@Payload CursorPositionDTO cursorUpdate, SimpMessageHeaderAccessor headerAccessor) {
//        try {
//            String roomCode = cursorUpdate.getRoomCode();
//            Long userId = cursorUpdate.getUserId();
//            if (roomCode == null || userId == null) {
//                log.warn("Missing roomCode or userId in cursor update payload");
//                return;
//            }
//            this.sessionRegistryService.updateUserActivity(roomCode, userId);
//            ParticipantEventDTO cursorEvent = ParticipantEventDTO.builder().eventType("CURSOR_UPDATE").userId(userId).roomCode(roomCode).cursorPosition(cursorUpdate).timestamp(System.currentTimeMillis()).build();
//            this.messagingTemplate.convertAndSend((Object)("/collab-topic/room/" + roomCode + "/cursors"), (Object)cursorEvent);
//        }
//        catch (Exception e) {
//            log.error("Error handling cursor update: {}", (Object)e.getMessage(), (Object)e);
//        }
//    }

//    @MessageMapping(value={"/room/selection"})
//    public void handleSelectionUpdate(@Payload SelectionInfoDTO selectionUpdate, SimpMessageHeaderAccessor headerAccessor) {
//        try {
//            String roomCode = selectionUpdate.getRoomCode();
//            Long userId = selectionUpdate.getUserId();
//            if (roomCode == null || userId == null) {
//                log.warn("Missing roomCode or userId in selection update payload");
//                return;
//            }
//            this.sessionRegistryService.updateUserActivity(roomCode, userId);
//            ParticipantEventDTO selectionEvent = ParticipantEventDTO.builder().eventType("SELECTION_UPDATE").userId(userId).roomCode(roomCode).selection(selectionUpdate).timestamp(System.currentTimeMillis()).build();
//            this.messagingTemplate.convertAndSend((Object)("/collab-topic/room/" + roomCode + "/selections"), (Object)selectionEvent);
//        }
//        catch (Exception e) {
//            log.error("Error handling selection update: {}", (Object)e.getMessage(), (Object)e);
//        }
//    }

//    @MessageMapping(value={"/room/code"})
//    public void handleCodeUpdate(@Payload CodeUpdateDTO codeUpdate, SimpMessageHeaderAccessor headerAccessor) {
//        try {
//            String roomCode = codeUpdate.getRoomCode();
//            Long userId = codeUpdate.getUserId();
//            if (roomCode == null || userId == null) {
//                log.warn("Missing roomCode or userId in code update payload");
//                return;
//            }
//            this.sessionRegistryService.updateUserActivity(roomCode, userId);
//            this.messagingTemplate.convertAndSend((Object)("/collab-topic/room/" + roomCode + "/code"), (Object)codeUpdate);
//            log.debug("Broadcasted code update for room {} from user {}", (Object)roomCode, (Object)userId);
//        }
//        catch (Exception e) {
//            log.error("Error handling code update: {}", (Object)e.getMessage(), (Object)e);
//        }
//    }

//    @MessageMapping(value={"/room/leave"})
//    public void handleLeaveRoom(@Payload JoinRoomRequestDTO leaveRequest, SimpMessageHeaderAccessor headerAccessor) {
//        try {
//            String roomCode = leaveRequest.getInviteCode();
//            Long userId = leaveRequest.getUserId();
//            if (roomCode == null || userId == null) {
//                log.warn("Missing roomCode or userId in leave request payload");
//                return;
//            }
//            this.sessionRegistryService.userLeft(roomCode, userId);
//            ParticipantEventDTO leaveEvent = ParticipantEventDTO.builder().eventType("USER_LEFT").userId(userId).roomCode(roomCode).timestamp(System.currentTimeMillis()).build();
//            this.messagingTemplate.convertAndSend((Object)("/collab-topic/room/" + roomCode + "/participants"), (Object)leaveEvent);
//            log.info("User {} left room {}", (Object)userId, (Object)roomCode);
//        }
//        catch (Exception e) {
//            log.error("Error handling leave room: {}", (Object)e.getMessage(), (Object)e);
//        }
//    }

//    private void broadcastRoomState(String roomCode) {
//        try {
//            List<ParticipantDto> participants = this.sessionRegistryService.getActiveParticipants(roomCode);
//            RoomDetailsResponse roomState = RoomDetailsResponse.builder().roomCode(roomCode).participants(participants).participantCount(participants.size()).timestamp(System.currentTimeMillis()).build();
//            this.messagingTemplate.convertAndSend((Object)("/collab-topic/room/" + roomCode + "/state"), (Object)roomState);
//            log.debug("Broadcasted room state for room {}", (Object)roomCode);
//        }
//        catch (Exception e) {
//            log.error("Error broadcasting room state for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
//        }
//    }

//    private void sendCurrentCodeState(String roomCode, Long userId) {
//        try {
//            String initialCode = "// Write your solution here\n";
//            String currentCode = this.otService.reconstructCurrentCode(roomCode, initialCode);
//            CodeStateDTO codeState = CodeStateDTO.builder().roomCode(roomCode).code(currentCode).language("java").version(Math.toIntExact(this.otService.getOperationCount(roomCode))).timestamp(System.currentTimeMillis()).build();
//            this.messagingTemplate.convertAndSend((Object)("/collab-topic/room/" + roomCode + "/code-state"), (Object)codeState);
//            log.debug("Sent current code state to room {}", (Object)roomCode);
//        }
//        catch (Exception e) {
//            log.error("Error sending current code state for room {}: {}", new Object[]{roomCode, e.getMessage(), e});
//        }
//    }

    @Generated
    public EditorController(SimpMessagingTemplate messagingTemplate, SessionRegistryService sessionRegistryService, OTServiceInterface otService, WebSocketSessionManager webSocketSessionManager) {
        this.messagingTemplate = messagingTemplate;
        this.sessionRegistryService = sessionRegistryService;
        this.otService = otService;
        this.webSocketSessionManager = webSocketSessionManager;
    }
}
