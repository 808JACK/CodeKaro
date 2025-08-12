/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class RoomActionDTO {
    private String roomCode;
    private Long userId;
    private String action;
    private Integer fromVersion;

    @Generated
    public static RoomActionDTOBuilder builder() {
        return new RoomActionDTOBuilder();
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
    public String getAction() {
        return this.action;
    }

    @Generated
    public Integer getFromVersion() {
        return this.fromVersion;
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
    public void setAction(String action) {
        this.action = action;
    }

    @Generated
    public void setFromVersion(Integer fromVersion) {
        this.fromVersion = fromVersion;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoomActionDTO)) {
            return false;
        }
        RoomActionDTO other = (RoomActionDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$fromVersion = this.getFromVersion();
        Integer other$fromVersion = other.getFromVersion();
        if (this$fromVersion == null ? other$fromVersion != null : !((Object)this$fromVersion).equals(other$fromVersion)) {
            return false;
        }
        String this$roomCode = this.getRoomCode();
        String other$roomCode = other.getRoomCode();
        if (this$roomCode == null ? other$roomCode != null : !this$roomCode.equals(other$roomCode)) {
            return false;
        }
        String this$action = this.getAction();
        String other$action = other.getAction();
        return !(this$action == null ? other$action != null : !this$action.equals(other$action));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RoomActionDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $fromVersion = this.getFromVersion();
        result = result * 59 + ($fromVersion == null ? 43 : ((Object)$fromVersion).hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        String $action = this.getAction();
        result = result * 59 + ($action == null ? 43 : $action.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "RoomActionDTO(roomCode=" + this.getRoomCode() + ", userId=" + this.getUserId() + ", action=" + this.getAction() + ", fromVersion=" + this.getFromVersion() + ")";
    }

    @Generated
    public RoomActionDTO() {
    }

    @Generated
    public RoomActionDTO(String roomCode, Long userId, String action, Integer fromVersion) {
        this.roomCode = roomCode;
        this.userId = userId;
        this.action = action;
        this.fromVersion = fromVersion;
    }

    @Generated
    public static class RoomActionDTOBuilder {
        @Generated
        private String roomCode;
        @Generated
        private Long userId;
        @Generated
        private String action;
        @Generated
        private Integer fromVersion;

        @Generated
        RoomActionDTOBuilder() {
        }

        @Generated
        public RoomActionDTOBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public RoomActionDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public RoomActionDTOBuilder action(String action) {
            this.action = action;
            return this;
        }

        @Generated
        public RoomActionDTOBuilder fromVersion(Integer fromVersion) {
            this.fromVersion = fromVersion;
            return this;
        }

        @Generated
        public RoomActionDTO build() {
            return new RoomActionDTO(this.roomCode, this.userId, this.action, this.fromVersion);
        }

        @Generated
        public String toString() {
            return "RoomActionDTO.RoomActionDTOBuilder(roomCode=" + this.roomCode + ", userId=" + this.userId + ", action=" + this.action + ", fromVersion=" + this.fromVersion + ")";
        }
    }
}
