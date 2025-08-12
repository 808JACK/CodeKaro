/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import com.example.demo.dtos.CursorPositionDTO;
import lombok.Generated;

public class SelectionInfoDTO {
    private String roomCode;
    private Long userId;
    private CursorPositionDTO startPosition;
    private CursorPositionDTO endPosition;
    private String selectedText;

    @Generated
    public static SelectionInfoDTOBuilder builder() {
        return new SelectionInfoDTOBuilder();
    }

    @Generated
    public String getRoomCode() {
        return this.roomCode;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public CursorPositionDTO getStartPosition() {
        return this.startPosition;
    }

    @Generated
    public CursorPositionDTO getEndPosition() {
        return this.endPosition;
    }

    @Generated
    public String getSelectedText() {
        return this.selectedText;
    }

    @Generated
    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setStartPosition(CursorPositionDTO startPosition) {
        this.startPosition = startPosition;
    }

    @Generated
    public void setEndPosition(CursorPositionDTO endPosition) {
        this.endPosition = endPosition;
    }

    @Generated
    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SelectionInfoDTO)) {
            return false;
        }
        SelectionInfoDTO other = (SelectionInfoDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        String this$roomCode = this.getRoomCode();
        String other$roomCode = other.getRoomCode();
        if (this$roomCode == null ? other$roomCode != null : !this$roomCode.equals(other$roomCode)) {
            return false;
        }
        CursorPositionDTO this$startPosition = this.getStartPosition();
        CursorPositionDTO other$startPosition = other.getStartPosition();
        if (this$startPosition == null ? other$startPosition != null : !((Object)this$startPosition).equals(other$startPosition)) {
            return false;
        }
        CursorPositionDTO this$endPosition = this.getEndPosition();
        CursorPositionDTO other$endPosition = other.getEndPosition();
        if (this$endPosition == null ? other$endPosition != null : !((Object)this$endPosition).equals(other$endPosition)) {
            return false;
        }
        String this$selectedText = this.getSelectedText();
        String other$selectedText = other.getSelectedText();
        return !(this$selectedText == null ? other$selectedText != null : !this$selectedText.equals(other$selectedText));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof SelectionInfoDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        CursorPositionDTO $startPosition = this.getStartPosition();
        result = result * 59 + ($startPosition == null ? 43 : ((Object)$startPosition).hashCode());
        CursorPositionDTO $endPosition = this.getEndPosition();
        result = result * 59 + ($endPosition == null ? 43 : ((Object)$endPosition).hashCode());
        String $selectedText = this.getSelectedText();
        result = result * 59 + ($selectedText == null ? 43 : $selectedText.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "SelectionInfoDTO(roomCode=" + this.getRoomCode() + ", userId=" + this.getUserId() + ", startPosition=" + String.valueOf(this.getStartPosition()) + ", endPosition=" + String.valueOf(this.getEndPosition()) + ", selectedText=" + this.getSelectedText() + ")";
    }

    @Generated
    public SelectionInfoDTO() {
    }

    @Generated
    public SelectionInfoDTO(String roomCode, Long userId, CursorPositionDTO startPosition, CursorPositionDTO endPosition, String selectedText) {
        this.roomCode = roomCode;
        this.userId = userId;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.selectedText = selectedText;
    }

    @Generated
    public static class SelectionInfoDTOBuilder {
        @Generated
        private String roomCode;
        @Generated
        private Long userId;
        @Generated
        private CursorPositionDTO startPosition;
        @Generated
        private CursorPositionDTO endPosition;
        @Generated
        private String selectedText;

        @Generated
        SelectionInfoDTOBuilder() {
        }

        @Generated
        public SelectionInfoDTOBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public SelectionInfoDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public SelectionInfoDTOBuilder startPosition(CursorPositionDTO startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        @Generated
        public SelectionInfoDTOBuilder endPosition(CursorPositionDTO endPosition) {
            this.endPosition = endPosition;
            return this;
        }

        @Generated
        public SelectionInfoDTOBuilder selectedText(String selectedText) {
            this.selectedText = selectedText;
            return this;
        }

        @Generated
        public SelectionInfoDTO build() {
            return new SelectionInfoDTO(this.roomCode, this.userId, this.startPosition, this.endPosition, this.selectedText);
        }

        @Generated
        public String toString() {
            return "SelectionInfoDTO.SelectionInfoDTOBuilder(roomCode=" + this.roomCode + ", userId=" + this.userId + ", startPosition=" + String.valueOf(this.startPosition) + ", endPosition=" + String.valueOf(this.endPosition) + ", selectedText=" + this.selectedText + ")";
        }
    }
}
