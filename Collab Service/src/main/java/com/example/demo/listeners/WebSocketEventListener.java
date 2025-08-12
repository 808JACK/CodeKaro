///*
// * Decompiled with CFR 0.152.
// *
// * Could not load the following classes:
// *  lombok.Generated
// *  org.slf4j.Logger
// *  org.slf4j.LoggerFactory
// *  org.springframework.context.event.EventListener
// *  org.springframework.messaging.simp.SimpMessageHeaderAccessor
// *  org.springframework.messaging.simp.SimpMessagingTemplate
// *  org.springframework.stereotype.Component
// *  org.springframework.web.socket.messaging.SessionConnectedEvent
// *  org.springframework.web.socket.messaging.SessionDisconnectEvent
// *  org.springframework.web.socket.messaging.SessionSubscribeEvent
// */
//package com.example.demo.listeners;
//
//import com.example.demo.services.SessionRegistryService;
//import com.example.demo.services.WebSocketSessionManager;
//import java.security.Principal;
//import java.util.Map;
//import lombok.Generated;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//import org.springframework.web.socket.messaging.SessionSubscribeEvent;
//
//@Component
//public class WebSocketEventListener {
//    @Generated
//    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);
//    private final SessionRegistryService sessionRegistryService;
//    private final WebSocketSessionManager webSocketSessionManager;
//    private final SimpMessagingTemplate messagingTemplate;
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        String sessionId = SimpMessageHeaderAccessor.getSessionId((Map)event.getMessage().getHeaders());
//        Principal userPrincipal = SimpMessageHeaderAccessor.getUser((Map)event.getMessage().getHeaders());
//        if (userPrincipal != null) {
//            log.info("WebSocket Connected: User={}, SessionId={}", (Object)userPrincipal.getName(), (Object)sessionId);
//        } else {
//            log.info("WebSocket Connected: SessionId={}", (Object)sessionId);
//        }
//    }
//
//    @EventListener
//    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
//        String sessionId = SimpMessageHeaderAccessor.getSessionId((Map)event.getMessage().getHeaders());
//        String destination = SimpMessageHeaderAccessor.getDestination((Map)event.getMessage().getHeaders());
//        Principal userPrincipal = SimpMessageHeaderAccessor.getUser((Map)event.getMessage().getHeaders());
//        if (userPrincipal != null) {
//            log.info("WebSocket Subscribed: User={}, SessionId={}, Destination={}", new Object[]{userPrincipal.getName(), sessionId, destination});
//        } else {
//            log.info("WebSocket Subscribed: SessionId={}, Destination={}", (Object)sessionId, (Object)destination);
//        }
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        block5: {
//            String sessionId = SimpMessageHeaderAccessor.getSessionId((Map)event.getMessage().getHeaders());
//            Principal userPrincipal = SimpMessageHeaderAccessor.getUser((Map)event.getMessage().getHeaders());
//            if (userPrincipal != null) {
//                String userId = userPrincipal.getName();
//                log.info("WebSocket Disconnected: User={}, SessionId={}", (Object)userId, (Object)sessionId);
//                try {
//                    String roomCode = this.webSocketSessionManager.getRoomForSession(sessionId);
//                    Long userIdLong = this.webSocketSessionManager.getUserForSession(sessionId);
//                    if (roomCode != null && userIdLong != null) {
//                        log.info("Cleaning up user {} from room {} due to disconnect", (Object)userIdLong, (Object)roomCode);
//                        this.sessionRegistryService.userLeft(roomCode, userIdLong);
//                        this.broadcastUserLeft(roomCode, userIdLong);
//                        log.info("Successfully cleaned up user {} from room {}", (Object)userIdLong, (Object)roomCode);
//                        break block5;
//                    }
//                    log.info("No room found for disconnected user {}, no cleanup needed", (Object)userId);
//                }
//                catch (Exception e) {
//                    log.error("Error during disconnect cleanup for user {}: {}", new Object[]{userId, e.getMessage(), e});
//                }
//            } else {
//                log.info("WebSocket Disconnected: SessionId={} (no user principal)", (Object)sessionId);
//            }
//        }
//    }
//
//    private void broadcastUserLeft(String roomCode, Long userId) {
//        try {
//            Map<String, Long> userLeftEvent = Map.of("eventType", "USER_LEFT", "userId", userId, "roomCode", roomCode, "timestamp", System.currentTimeMillis());
//            String destination = "/collab-topic/room/" + roomCode + "/participants";
//            this.messagingTemplate.convertAndSend((Object)destination, userLeftEvent);
//            log.debug("Broadcasted user left event for user {} in room {}", (Object)userId, (Object)roomCode);
//        }
//        catch (Exception e) {
//            log.error("Error broadcasting user left event for user {} in room {}: {}", new Object[]{userId, roomCode, e.getMessage(), e});
//        }
//    }
//
//    @Generated
//    public WebSocketEventListener(SessionRegistryService sessionRegistryService, WebSocketSessionManager webSocketSessionManager, SimpMessagingTemplate messagingTemplate) {
//        this.sessionRegistryService = sessionRegistryService;
//        this.webSocketSessionManager = webSocketSessionManager;
//        this.messagingTemplate = messagingTemplate;
//    }
//}
