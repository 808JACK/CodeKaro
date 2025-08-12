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

import com.example.demo.dtos.ChatMessageDTO;
import com.example.demo.services.SessionRegistryService;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NormalChatController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(NormalChatController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRegistryService sessionRegistryService;

    @MessageMapping(value={"/room/chat"})
    public void handleChatMessage(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String roomCode = chatMessage.getRoomCode();
            Long userId = chatMessage.getSenderId();
            if (roomCode == null || userId == null) {
                log.warn("Missing roomCode or userId in chat message payload");
                return;
            }
            if (!this.sessionRegistryService.isUserInRoom(roomCode, userId)) {
                log.warn("User {} not in room {} - rejecting chat message", (Object)userId, (Object)roomCode);
                return;
            }
            this.sessionRegistryService.updateUserActivity(roomCode, userId);
            chatMessage.setRoomCode(roomCode);
            chatMessage.setSenderId(userId);
            chatMessage.setTimestamp(System.currentTimeMillis());
            this.messagingTemplate.convertAndSend((String) ("/collab-topic/room/" + roomCode + "/chat"), (Object)chatMessage);
            log.debug("Broadcasted chat message from user {} in room {}", (Object)userId, (Object)roomCode);
        }
        catch (Exception e) {
            log.error("Error handling chat message: {}", (Object)e.getMessage(), (Object)e);
        }
    }

    @MessageMapping(value={"/room/typing"})
    public void handleTypingIndicator(@Payload String typingStatus, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String roomCode = (String)headerAccessor.getSessionAttributes().get("roomCode");
            Long userId = (Long)headerAccessor.getSessionAttributes().get("userId");
            if (roomCode == null || userId == null) {
                log.warn("Missing roomCode or userId in session attributes for typing indicator");
                return;
            }
            ChatMessageDTO typingEvent = ChatMessageDTO.builder().messageType("TYPING").senderId(userId).roomCode(roomCode).content(typingStatus).timestamp(System.currentTimeMillis()).build();
            this.messagingTemplate.convertAndSend((String) ("/collab-topic/room/" + roomCode + "/typing"), (Object)typingEvent);
        }
        catch (Exception e) {
            log.error("Error handling typing indicator: {}", (Object)e.getMessage(), (Object)e);
        }
    }

    @Generated
    public NormalChatController(SimpMessagingTemplate messagingTemplate, SessionRegistryService sessionRegistryService) {
        this.messagingTemplate = messagingTemplate;
        this.sessionRegistryService = sessionRegistryService;
    }
}
