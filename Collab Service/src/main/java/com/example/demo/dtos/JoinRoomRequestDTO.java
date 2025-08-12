/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class JoinRoomRequestDTO {
    private String inviteCode;
    private Long userId;

    @Generated
    public static JoinRoomRequestDTOBuilder builder() {
        return new JoinRoomRequestDTOBuilder();
    }

    @Generated
    public String getInviteCode() {
        return this.inviteCode;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof JoinRoomRequestDTO)) {
            return false;
        }
        JoinRoomRequestDTO other = (JoinRoomRequestDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        String this$inviteCode = this.getInviteCode();
        String other$inviteCode = other.getInviteCode();
        return !(this$inviteCode == null ? other$inviteCode != null : !this$inviteCode.equals(other$inviteCode));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof JoinRoomRequestDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        String $inviteCode = this.getInviteCode();
        result = result * 59 + ($inviteCode == null ? 43 : $inviteCode.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "JoinRoomRequestDTO(inviteCode=" + this.getInviteCode() + ", userId=" + this.getUserId() + ")";
    }

    @Generated
    public JoinRoomRequestDTO() {
    }

    @Generated
    public JoinRoomRequestDTO(String inviteCode, Long userId) {
        this.inviteCode = inviteCode;
        this.userId = userId;
    }

    @Generated
    public static class JoinRoomRequestDTOBuilder {
        @Generated
        private String inviteCode;
        @Generated
        private Long userId;

        @Generated
        JoinRoomRequestDTOBuilder() {
        }

        @Generated
        public JoinRoomRequestDTOBuilder inviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
            return this;
        }

        @Generated
        public JoinRoomRequestDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public JoinRoomRequestDTO build() {
            return new JoinRoomRequestDTO(this.inviteCode, this.userId);
        }

        @Generated
        public String toString() {
            return "JoinRoomRequestDTO.JoinRoomRequestDTOBuilder(inviteCode=" + this.inviteCode + ", userId=" + this.userId + ")";
        }
    }
}
