/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import com.example.demo.dtos.CursorPositionDTO;
import com.example.demo.dtos.SelectionInfoDTO;
import lombok.Generated;

public class ParticipantEventDTO {
    private String eventType;
    private Long userId;
    private String userName;
    private String roomCode;
    private Long timestamp;
    private CursorPositionDTO cursorPosition;
    private SelectionInfoDTO selection;

    @Generated
    public static ParticipantEventDTOBuilder builder() {
        return new ParticipantEventDTOBuilder();
    }

    @Generated
    public String getEventType() {
        return this.eventType;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public String getUserName() {
        return this.userName;
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
    public CursorPositionDTO getCursorPosition() {
        return this.cursorPosition;
    }

    @Generated
    public SelectionInfoDTO getSelection() {
        return this.selection;
    }

    @Generated
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setUserName(String userName) {
        this.userName = userName;
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
    public void setCursorPosition(CursorPositionDTO cursorPosition) {
        this.cursorPosition = cursorPosition;
    }

    @Generated
    public void setSelection(SelectionInfoDTO selection) {
        this.selection = selection;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ParticipantEventDTO)) {
            return false;
        }
        ParticipantEventDTO other = (ParticipantEventDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Long this$timestamp = this.getTimestamp();
        Long other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        String this$eventType = this.getEventType();
        String other$eventType = other.getEventType();
        if (this$eventType == null ? other$eventType != null : !this$eventType.equals(other$eventType)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$roomCode = this.getRoomCode();
        String other$roomCode = other.getRoomCode();
        if (this$roomCode == null ? other$roomCode != null : !this$roomCode.equals(other$roomCode)) {
            return false;
        }
        CursorPositionDTO this$cursorPosition = this.getCursorPosition();
        CursorPositionDTO other$cursorPosition = other.getCursorPosition();
        if (this$cursorPosition == null ? other$cursorPosition != null : !((Object)this$cursorPosition).equals(other$cursorPosition)) {
            return false;
        }
        SelectionInfoDTO this$selection = this.getSelection();
        SelectionInfoDTO other$selection = other.getSelection();
        return !(this$selection == null ? other$selection != null : !((Object)this$selection).equals(other$selection));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ParticipantEventDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Long $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        String $eventType = this.getEventType();
        result = result * 59 + ($eventType == null ? 43 : $eventType.hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        CursorPositionDTO $cursorPosition = this.getCursorPosition();
        result = result * 59 + ($cursorPosition == null ? 43 : ((Object)$cursorPosition).hashCode());
        SelectionInfoDTO $selection = this.getSelection();
        result = result * 59 + ($selection == null ? 43 : ((Object)$selection).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ParticipantEventDTO(eventType=" + this.getEventType() + ", userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", roomCode=" + this.getRoomCode() + ", timestamp=" + this.getTimestamp() + ", cursorPosition=" + String.valueOf(this.getCursorPosition()) + ", selection=" + String.valueOf(this.getSelection()) + ")";
    }

    @Generated
    public ParticipantEventDTO() {
    }

    @Generated
    public ParticipantEventDTO(String eventType, Long userId, String userName, String roomCode, Long timestamp, CursorPositionDTO cursorPosition, SelectionInfoDTO selection) {
        this.eventType = eventType;
        this.userId = userId;
        this.userName = userName;
        this.roomCode = roomCode;
        this.timestamp = timestamp;
        this.cursorPosition = cursorPosition;
        this.selection = selection;
    }

    @Generated
    public static class ParticipantEventDTOBuilder {
        @Generated
        private String eventType;
        @Generated
        private Long userId;
        @Generated
        private String userName;
        @Generated
        private String roomCode;
        @Generated
        private Long timestamp;
        @Generated
        private CursorPositionDTO cursorPosition;
        @Generated
        private SelectionInfoDTO selection;

        @Generated
        ParticipantEventDTOBuilder() {
        }

        @Generated
        public ParticipantEventDTOBuilder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        @Generated
        public ParticipantEventDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public ParticipantEventDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        @Generated
        public ParticipantEventDTOBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public ParticipantEventDTOBuilder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public ParticipantEventDTOBuilder cursorPosition(CursorPositionDTO cursorPosition) {
            this.cursorPosition = cursorPosition;
            return this;
        }

        @Generated
        public ParticipantEventDTOBuilder selection(SelectionInfoDTO selection) {
            this.selection = selection;
            return this;
        }

        @Generated
        public ParticipantEventDTO build() {
            return new ParticipantEventDTO(this.eventType, this.userId, this.userName, this.roomCode, this.timestamp, this.cursorPosition, this.selection);
        }

        @Generated
        public String toString() {
            return "ParticipantEventDTO.ParticipantEventDTOBuilder(eventType=" + this.eventType + ", userId=" + this.userId + ", userName=" + this.userName + ", roomCode=" + this.roomCode + ", timestamp=" + this.timestamp + ", cursorPosition=" + String.valueOf(this.cursorPosition) + ", selection=" + String.valueOf(this.selection) + ")";
        }
    }
}
