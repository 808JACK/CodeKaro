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

import com.example.demo.dtos.RoomActionDTO;
import com.example.demo.services.OTServiceInterface;
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
public class WebSocketController {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WebSocketController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final SessionRegistryService sessionRegistryService;
    private final OTServiceInterface otService;

    @MessageMapping(value={"/room/ping"})
    public void handlePing(@Payload RoomActionDTO pingRequest, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String roomCode = pingRequest.getRoomCode();
            Long userId = pingRequest.getUserId();
            if (roomCode != null && userId != null) {
                this.sessionRegistryService.updateUserActivity(roomCode, userId);
                this.messagingTemplate.convertAndSendToUser(headerAccessor.getSessionId(), "/collab-queue/pong", (Object)"pong");
            }
        }
        catch (Exception e) {
            log.error("Error handling ping: {}", (Object)e.getMessage(), (Object)e);
        }
    }

    @Generated
    public WebSocketController(SimpMessagingTemplate messagingTemplate, SessionRegistryService sessionRegistryService, OTServiceInterface otService) {
        this.messagingTemplate = messagingTemplate;
        this.sessionRegistryService = sessionRegistryService;
        this.otService = otService;
    }
}
