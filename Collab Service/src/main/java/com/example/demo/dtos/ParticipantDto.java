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

public class ParticipantDto {
    private Long userId;
    private String userName;
    private String userColor;
    private String avatarUrl;
    private Long joinedAt;
    private Long lastActiveAt;
    private Boolean isActive;
    private CursorPositionDTO cursorPosition;
    private SelectionInfoDTO selection;

    @Generated
    public static ParticipantDtoBuilder builder() {
        return new ParticipantDtoBuilder();
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
    public String getUserColor() {
        return this.userColor;
    }

    @Generated
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    @Generated
    public Long getJoinedAt() {
        return this.joinedAt;
    }

    @Generated
    public Long getLastActiveAt() {
        return this.lastActiveAt;
    }

    @Generated
    public Boolean getIsActive() {
        return this.isActive;
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
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated
    public void setUserColor(String userColor) {
        this.userColor = userColor;
    }

    @Generated
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Generated
    public void setJoinedAt(Long joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Generated
    public void setLastActiveAt(Long lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    @Generated
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
        if (!(o instanceof ParticipantDto)) {
            return false;
        }
        ParticipantDto other = (ParticipantDto)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Long this$joinedAt = this.getJoinedAt();
        Long other$joinedAt = other.getJoinedAt();
        if (this$joinedAt == null ? other$joinedAt != null : !((Object)this$joinedAt).equals(other$joinedAt)) {
            return false;
        }
        Long this$lastActiveAt = this.getLastActiveAt();
        Long other$lastActiveAt = other.getLastActiveAt();
        if (this$lastActiveAt == null ? other$lastActiveAt != null : !((Object)this$lastActiveAt).equals(other$lastActiveAt)) {
            return false;
        }
        Boolean this$isActive = this.getIsActive();
        Boolean other$isActive = other.getIsActive();
        if (this$isActive == null ? other$isActive != null : !((Object)this$isActive).equals(other$isActive)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$userColor = this.getUserColor();
        String other$userColor = other.getUserColor();
        if (this$userColor == null ? other$userColor != null : !this$userColor.equals(other$userColor)) {
            return false;
        }
        String this$avatarUrl = this.getAvatarUrl();
        String other$avatarUrl = other.getAvatarUrl();
        if (this$avatarUrl == null ? other$avatarUrl != null : !this$avatarUrl.equals(other$avatarUrl)) {
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
        return other instanceof ParticipantDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Long $joinedAt = this.getJoinedAt();
        result = result * 59 + ($joinedAt == null ? 43 : ((Object)$joinedAt).hashCode());
        Long $lastActiveAt = this.getLastActiveAt();
        result = result * 59 + ($lastActiveAt == null ? 43 : ((Object)$lastActiveAt).hashCode());
        Boolean $isActive = this.getIsActive();
        result = result * 59 + ($isActive == null ? 43 : ((Object)$isActive).hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $userColor = this.getUserColor();
        result = result * 59 + ($userColor == null ? 43 : $userColor.hashCode());
        String $avatarUrl = this.getAvatarUrl();
        result = result * 59 + ($avatarUrl == null ? 43 : $avatarUrl.hashCode());
        CursorPositionDTO $cursorPosition = this.getCursorPosition();
        result = result * 59 + ($cursorPosition == null ? 43 : ((Object)$cursorPosition).hashCode());
        SelectionInfoDTO $selection = this.getSelection();
        result = result * 59 + ($selection == null ? 43 : ((Object)$selection).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ParticipantDto(userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", userColor=" + this.getUserColor() + ", avatarUrl=" + this.getAvatarUrl() + ", joinedAt=" + this.getJoinedAt() + ", lastActiveAt=" + this.getLastActiveAt() + ", isActive=" + this.getIsActive() + ", cursorPosition=" + String.valueOf(this.getCursorPosition()) + ", selection=" + String.valueOf(this.getSelection()) + ")";
    }

    @Generated
    public ParticipantDto() {
    }

    @Generated
    public ParticipantDto(Long userId, String userName, String userColor, String avatarUrl, Long joinedAt, Long lastActiveAt, Boolean isActive, CursorPositionDTO cursorPosition, SelectionInfoDTO selection) {
        this.userId = userId;
        this.userName = userName;
        this.userColor = userColor;
        this.avatarUrl = avatarUrl;
        this.joinedAt = joinedAt;
        this.lastActiveAt = lastActiveAt;
        this.isActive = isActive;
        this.cursorPosition = cursorPosition;
        this.selection = selection;
    }

    @Generated
    public static class ParticipantDtoBuilder {
        @Generated
        private Long userId;
        @Generated
        private String userName;
        @Generated
        private String userColor;
        @Generated
        private String avatarUrl;
        @Generated
        private Long joinedAt;
        @Generated
        private Long lastActiveAt;
        @Generated
        private Boolean isActive;
        @Generated
        private CursorPositionDTO cursorPosition;
        @Generated
        private SelectionInfoDTO selection;

        @Generated
        ParticipantDtoBuilder() {
        }

        @Generated
        public ParticipantDtoBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public ParticipantDtoBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        @Generated
        public ParticipantDtoBuilder userColor(String userColor) {
            this.userColor = userColor;
            return this;
        }

        @Generated
        public ParticipantDtoBuilder avatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        @Generated
        public ParticipantDtoBuilder joinedAt(Long joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        @Generated
        public ParticipantDtoBuilder lastActiveAt(Long lastActiveAt) {
            this.lastActiveAt = lastActiveAt;
            return this;
        }

        @Generated
        public ParticipantDtoBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        @Generated
        public ParticipantDtoBuilder cursorPosition(CursorPositionDTO cursorPosition) {
            this.cursorPosition = cursorPosition;
            return this;
        }

        @Generated
        public ParticipantDtoBuilder selection(SelectionInfoDTO selection) {
            this.selection = selection;
            return this;
        }

        @Generated
        public ParticipantDto build() {
            return new ParticipantDto(this.userId, this.userName, this.userColor, this.avatarUrl, this.joinedAt, this.lastActiveAt, this.isActive, this.cursorPosition, this.selection);
        }

        @Generated
        public String toString() {
            return "ParticipantDto.ParticipantDtoBuilder(userId=" + this.userId + ", userName=" + this.userName + ", userColor=" + this.userColor + ", avatarUrl=" + this.avatarUrl + ", joinedAt=" + this.joinedAt + ", lastActiveAt=" + this.lastActiveAt + ", isActive=" + this.isActive + ", cursorPosition=" + String.valueOf(this.cursorPosition) + ", selection=" + String.valueOf(this.selection) + ")";
        }
    }
}
