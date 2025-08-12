/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class CursorPositionDTO {
    private String roomCode;
    private Long userId;
    private Integer lineNumber;
    private Integer column;

    @Generated
    public static CursorPositionDTOBuilder builder() {
        return new CursorPositionDTOBuilder();
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
    public Integer getLineNumber() {
        return this.lineNumber;
    }

    @Generated
    public Integer getColumn() {
        return this.column;
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
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Generated
    public void setColumn(Integer column) {
        this.column = column;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CursorPositionDTO)) {
            return false;
        }
        CursorPositionDTO other = (CursorPositionDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$lineNumber = this.getLineNumber();
        Integer other$lineNumber = other.getLineNumber();
        if (this$lineNumber == null ? other$lineNumber != null : !((Object)this$lineNumber).equals(other$lineNumber)) {
            return false;
        }
        Integer this$column = this.getColumn();
        Integer other$column = other.getColumn();
        if (this$column == null ? other$column != null : !((Object)this$column).equals(other$column)) {
            return false;
        }
        String this$roomCode = this.getRoomCode();
        String other$roomCode = other.getRoomCode();
        return !(this$roomCode == null ? other$roomCode != null : !this$roomCode.equals(other$roomCode));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CursorPositionDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $lineNumber = this.getLineNumber();
        result = result * 59 + ($lineNumber == null ? 43 : ((Object)$lineNumber).hashCode());
        Integer $column = this.getColumn();
        result = result * 59 + ($column == null ? 43 : ((Object)$column).hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CursorPositionDTO(roomCode=" + this.getRoomCode() + ", userId=" + this.getUserId() + ", lineNumber=" + this.getLineNumber() + ", column=" + this.getColumn() + ")";
    }

    @Generated
    public CursorPositionDTO() {
    }

    @Generated
    public CursorPositionDTO(String roomCode, Long userId, Integer lineNumber, Integer column) {
        this.roomCode = roomCode;
        this.userId = userId;
        this.lineNumber = lineNumber;
        this.column = column;
    }

    @Generated
    public static class CursorPositionDTOBuilder {
        @Generated
        private String roomCode;
        @Generated
        private Long userId;
        @Generated
        private Integer lineNumber;
        @Generated
        private Integer column;

        @Generated
        CursorPositionDTOBuilder() {
        }

        @Generated
        public CursorPositionDTOBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public CursorPositionDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public CursorPositionDTOBuilder lineNumber(Integer lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        @Generated
        public CursorPositionDTOBuilder column(Integer column) {
            this.column = column;
            return this;
        }

        @Generated
        public CursorPositionDTO build() {
            return new CursorPositionDTO(this.roomCode, this.userId, this.lineNumber, this.column);
        }

        @Generated
        public String toString() {
            return "CursorPositionDTO.CursorPositionDTOBuilder(roomCode=" + this.roomCode + ", userId=" + this.userId + ", lineNumber=" + this.lineNumber + ", column=" + this.column + ")";
        }
    }
}
