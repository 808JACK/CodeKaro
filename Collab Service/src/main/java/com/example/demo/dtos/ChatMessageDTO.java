/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class ChatMessageDTO {
    private String messageType;
    private Long senderId;
    private String senderName;
    private String content;
    private String roomCode;
    private Long timestamp;

    @Generated
    public static ChatMessageDTOBuilder builder() {
        return new ChatMessageDTOBuilder();
    }

    @Generated
    public String getMessageType() {
        return this.messageType;
    }

    @Generated
    public Long getSenderId() {
        return this.senderId;
    }

    @Generated
    public String getSenderName() {
        return this.senderName;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public String getRoomCode() {
        return this.roomCode;
    }

    @Generated
    public Long getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Generated
    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    @Generated
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    @Generated
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ChatMessageDTO)) {
            return false;
        }
        ChatMessageDTO other = (ChatMessageDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$senderId = this.getSenderId();
        Long other$senderId = other.getSenderId();
        if (this$senderId == null ? other$senderId != null : !((Object)this$senderId).equals(other$senderId)) {
            return false;
        }
        Long this$timestamp = this.getTimestamp();
        Long other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        String this$messageType = this.getMessageType();
        String other$messageType = other.getMessageType();
        if (this$messageType == null ? other$messageType != null : !this$messageType.equals(other$messageType)) {
            return false;
        }
        String this$senderName = this.getSenderName();
        String other$senderName = other.getSenderName();
        if (this$senderName == null ? other$senderName != null : !this$senderName.equals(other$senderName)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$roomCode = this.getRoomCode();
        String other$roomCode = other.getRoomCode();
        return !(this$roomCode == null ? other$roomCode != null : !this$roomCode.equals(other$roomCode));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ChatMessageDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $senderId = this.getSenderId();
        result = result * 59 + ($senderId == null ? 43 : ((Object)$senderId).hashCode());
        Long $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        String $messageType = this.getMessageType();
        result = result * 59 + ($messageType == null ? 43 : $messageType.hashCode());
        String $senderName = this.getSenderName();
        result = result * 59 + ($senderName == null ? 43 : $senderName.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ChatMessageDTO(messageType=" + this.getMessageType() + ", senderId=" + this.getSenderId() + ", senderName=" + this.getSenderName() + ", content=" + this.getContent() + ", roomCode=" + this.getRoomCode() + ", timestamp=" + this.getTimestamp() + ")";
    }

    @Generated
    public ChatMessageDTO() {
    }

    @Generated
    public ChatMessageDTO(String messageType, Long senderId, String senderName, String content, String roomCode, Long timestamp) {
        this.messageType = messageType;
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.roomCode = roomCode;
        this.timestamp = timestamp;
    }

    @Generated
    public static class ChatMessageDTOBuilder {
        @Generated
        private String messageType;
        @Generated
        private Long senderId;
        @Generated
        private String senderName;
        @Generated
        private String content;
        @Generated
        private String roomCode;
        @Generated
        private Long timestamp;

        @Generated
        ChatMessageDTOBuilder() {
        }

        @Generated
        public ChatMessageDTOBuilder messageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        @Generated
        public ChatMessageDTOBuilder senderId(Long senderId) {
            this.senderId = senderId;
            return this;
        }

        @Generated
        public ChatMessageDTOBuilder senderName(String senderName) {
            this.senderName = senderName;
            return this;
        }

        @Generated
        public ChatMessageDTOBuilder content(String content) {
            this.content = content;
            return this;
        }

        @Generated
        public ChatMessageDTOBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public ChatMessageDTOBuilder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public ChatMessageDTO build() {
            return new ChatMessageDTO(this.messageType, this.senderId, this.senderName, this.content, this.roomCode, this.timestamp);
        }

        @Generated
        public String toString() {
            return "ChatMessageDTO.ChatMessageDTOBuilder(messageType=" + this.messageType + ", senderId=" + this.senderId + ", senderName=" + this.senderName + ", content=" + this.content + ", roomCode=" + this.roomCode + ", timestamp=" + this.timestamp + ")";
        }
    }
}
