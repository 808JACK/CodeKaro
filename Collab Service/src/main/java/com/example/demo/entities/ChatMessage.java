/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.index.Indexed
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.example.demo.entities;

import java.time.Instant;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

// @Document(collection="chat_messages") - Disabled for PostgreSQL-only setup
public class ChatMessage {
    @Id
    private String id;
    // @Indexed - Disabled for PostgreSQL-only setup
    private Long roomId;
    private Long userId;
    private String username;
    private String content;
    private Instant timestamp;

    @Generated
    public static ChatMessageBuilder builder() {
        return new ChatMessageBuilder();
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public Long getRoomId() {
        return this.roomId;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public Instant getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setUsername(String username) {
        this.username = username;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChatMessage)) {
            return false;
        }
        ChatMessage other = (ChatMessage)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$roomId = this.getRoomId();
        Long other$roomId = other.getRoomId();
        if (this$roomId == null ? other$roomId != null : !((Object)this$roomId).equals(other$roomId)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        Instant this$timestamp = this.getTimestamp();
        Instant other$timestamp = other.getTimestamp();
        return !(this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatMessage;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $roomId = this.getRoomId();
        result = result * 59 + ($roomId == null ? 43 : ((Object)$roomId).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        Instant $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ChatMessage(id=" + this.getId() + ", roomId=" + this.getRoomId() + ", userId=" + this.getUserId() + ", username=" + this.getUsername() + ", content=" + this.getContent() + ", timestamp=" + String.valueOf(this.getTimestamp()) + ")";
    }

    @Generated
    public ChatMessage() {
    }

    @Generated
    public ChatMessage(String id, Long roomId, Long userId, String username, String content, Instant timestamp) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Generated
    public static class ChatMessageBuilder {
        @Generated
        private String id;
        @Generated
        private Long roomId;
        @Generated
        private Long userId;
        @Generated
        private String username;
        @Generated
        private String content;
        @Generated
        private Instant timestamp;

        @Generated
        ChatMessageBuilder() {
        }

        @Generated
        public ChatMessageBuilder id(String id) {
            this.id = id;
            return this;
        }

        @Generated
        public ChatMessageBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        @Generated
        public ChatMessageBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public ChatMessageBuilder username(String username) {
            this.username = username;
            return this;
        }

        @Generated
        public ChatMessageBuilder content(String content) {
            this.content = content;
            return this;
        }

        @Generated
        public ChatMessageBuilder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public ChatMessage build() {
            return new ChatMessage(this.id, this.roomId, this.userId, this.username, this.content, this.timestamp);
        }

        @Generated
        public String toString() {
            return "ChatMessage.ChatMessageBuilder(id=" + this.id + ", roomId=" + this.roomId + ", userId=" + this.userId + ", username=" + this.username + ", content=" + this.content + ", timestamp=" + String.valueOf(this.timestamp) + ")";
        }
    }
}
