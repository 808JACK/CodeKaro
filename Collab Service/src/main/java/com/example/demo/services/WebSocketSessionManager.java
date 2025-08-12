/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 *  org.springframework.web.socket.WebSocketSession
 */
package com.example.demo.services;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class WebSocketSessionManager {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(WebSocketSessionManager.class);
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<String, Set<WebSocketSession>>();
    private final Map<String, String> sessionToRoom = new ConcurrentHashMap<String, String>();
    private final Map<String, Long> sessionToUser = new ConcurrentHashMap<String, Long>();

    public void addSessionToRoom(String roomCode, WebSocketSession session, Long userId) {
        try {
            this.roomSessions.computeIfAbsent(roomCode, k -> new CopyOnWriteArraySet()).add(session);
            this.sessionToRoom.put(session.getId(), roomCode);
            this.sessionToUser.put(session.getId(), userId);
            log.info("Added WebSocket session {} to room {} for user {}", new Object[]{session.getId(), roomCode, userId});
        }
        catch (Exception e) {
            log.error("Error adding session {} to room {}: {}", new Object[]{session.getId(), roomCode, e.getMessage(), e});
        }
    }

    public void addSessionToRoom(String roomCode, String sessionId, Long userId) {
        try {
            this.sessionToRoom.put(sessionId, roomCode);
            this.sessionToUser.put(sessionId, userId);
            log.info("Added session {} to room {} for user {}", new Object[]{sessionId, roomCode, userId});
        }
        catch (Exception e) {
            log.error("Error adding session {} to room {}: {}", new Object[]{sessionId, roomCode, e.getMessage(), e});
        }
    }

    public void removeSession(WebSocketSession session) {
        try {
            String sessionId = session.getId();
            String roomCode = this.sessionToRoom.remove(sessionId);
            Long userId = this.sessionToUser.remove(sessionId);
            if (roomCode != null) {
                Set<WebSocketSession> sessions = this.roomSessions.get(roomCode);
                if (sessions != null) {
                    sessions.remove(session);
                    if (sessions.isEmpty()) {
                        this.roomSessions.remove(roomCode);
                    }
                }
                log.info("Removed WebSocket session {} from room {} for user {}", new Object[]{sessionId, roomCode, userId});
            }
        }
        catch (Exception e) {
            log.error("Error removing session {}: {}", new Object[]{session.getId(), e.getMessage(), e});
        }
    }

    public Set<WebSocketSession> getSessionsInRoom(String roomCode) {
        return this.roomSessions.getOrDefault(roomCode, new CopyOnWriteArraySet());
    }

    public String getRoomForSession(String sessionId) {
        return this.sessionToRoom.get(sessionId);
    }

    public Long getUserForSession(String sessionId) {
        return this.sessionToUser.get(sessionId);
    }

    public int getSessionCountInRoom(String roomCode) {
        Set<WebSocketSession> sessions = this.roomSessions.get(roomCode);
        return sessions != null ? sessions.size() : 0;
    }

    public boolean isSessionInRoom(String sessionId, String roomCode) {
        return roomCode.equals(this.sessionToRoom.get(sessionId));
    }

    @Generated
    public WebSocketSessionManager() {
    }
}
