/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class RoomCreatedResponse {
    Long roomId;
    String inviteCode;

    @Generated
    public static RoomCreatedResponseBuilder builder() {
        return new RoomCreatedResponseBuilder();
    }

    @Generated
    public Long getRoomId() {
        return this.roomId;
    }

    @Generated
    public String getInviteCode() {
        return this.inviteCode;
    }

    @Generated
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Generated
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoomCreatedResponse)) {
            return false;
        }
        RoomCreatedResponse other = (RoomCreatedResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$roomId = this.getRoomId();
        Long other$roomId = other.getRoomId();
        if (this$roomId == null ? other$roomId != null : !((Object)this$roomId).equals(other$roomId)) {
            return false;
        }
        String this$inviteCode = this.getInviteCode();
        String other$inviteCode = other.getInviteCode();
        return !(this$inviteCode == null ? other$inviteCode != null : !this$inviteCode.equals(other$inviteCode));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RoomCreatedResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $roomId = this.getRoomId();
        result = result * 59 + ($roomId == null ? 43 : ((Object)$roomId).hashCode());
        String $inviteCode = this.getInviteCode();
        result = result * 59 + ($inviteCode == null ? 43 : $inviteCode.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "RoomCreatedResponse(roomId=" + this.getRoomId() + ", inviteCode=" + this.getInviteCode() + ")";
    }

    @Generated
    public RoomCreatedResponse() {
    }

    @Generated
    public RoomCreatedResponse(Long roomId, String inviteCode) {
        this.roomId = roomId;
        this.inviteCode = inviteCode;
    }

    @Generated
    public static class RoomCreatedResponseBuilder {
        @Generated
        private Long roomId;
        @Generated
        private String inviteCode;

        @Generated
        RoomCreatedResponseBuilder() {
        }

        @Generated
        public RoomCreatedResponseBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        @Generated
        public RoomCreatedResponseBuilder inviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
            return this;
        }

        @Generated
        public RoomCreatedResponse build() {
            return new RoomCreatedResponse(this.roomId, this.inviteCode);
        }

        @Generated
        public String toString() {
            return "RoomCreatedResponse.RoomCreatedResponseBuilder(roomId=" + this.roomId + ", inviteCode=" + this.inviteCode + ")";
        }
    }
}
