/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class CodeUpdateDTO {
    private Long userId;
    private String roomCode;
    private String code;
    private String language;
    private String updateType;
    private Long timestamp;

    @Generated
    public static CodeUpdateDTOBuilder builder() {
        return new CodeUpdateDTOBuilder();
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public String getRoomCode() {
        return this.roomCode;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public String getUpdateType() {
        return this.updateType;
    }

    @Generated
    public Long getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    @Generated
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
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
        if (!(o instanceof CodeUpdateDTO)) {
            return false;
        }
        CodeUpdateDTO other = (CodeUpdateDTO)o;
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
        String this$roomCode = this.getRoomCode();
        String other$roomCode = other.getRoomCode();
        if (this$roomCode == null ? other$roomCode != null : !this$roomCode.equals(other$roomCode)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$language = this.getLanguage();
        String other$language = other.getLanguage();
        if (this$language == null ? other$language != null : !this$language.equals(other$language)) {
            return false;
        }
        String this$updateType = this.getUpdateType();
        String other$updateType = other.getUpdateType();
        return !(this$updateType == null ? other$updateType != null : !this$updateType.equals(other$updateType));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodeUpdateDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Long $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        String $updateType = this.getUpdateType();
        result = result * 59 + ($updateType == null ? 43 : $updateType.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CodeUpdateDTO(userId=" + this.getUserId() + ", roomCode=" + this.getRoomCode() + ", code=" + this.getCode() + ", language=" + this.getLanguage() + ", updateType=" + this.getUpdateType() + ", timestamp=" + this.getTimestamp() + ")";
    }

    @Generated
    public CodeUpdateDTO() {
    }

    @Generated
    public CodeUpdateDTO(Long userId, String roomCode, String code, String language, String updateType, Long timestamp) {
        this.userId = userId;
        this.roomCode = roomCode;
        this.code = code;
        this.language = language;
        this.updateType = updateType;
        this.timestamp = timestamp;
    }

    @Generated
    public static class CodeUpdateDTOBuilder {
        @Generated
        private Long userId;
        @Generated
        private String roomCode;
        @Generated
        private String code;
        @Generated
        private String language;
        @Generated
        private String updateType;
        @Generated
        private Long timestamp;

        @Generated
        CodeUpdateDTOBuilder() {
        }

        @Generated
        public CodeUpdateDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public CodeUpdateDTOBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public CodeUpdateDTOBuilder code(String code) {
            this.code = code;
            return this;
        }

        @Generated
        public CodeUpdateDTOBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public CodeUpdateDTOBuilder updateType(String updateType) {
            this.updateType = updateType;
            return this;
        }

        @Generated
        public CodeUpdateDTOBuilder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public CodeUpdateDTO build() {
            return new CodeUpdateDTO(this.userId, this.roomCode, this.code, this.language, this.updateType, this.timestamp);
        }

        @Generated
        public String toString() {
            return "CodeUpdateDTO.CodeUpdateDTOBuilder(userId=" + this.userId + ", roomCode=" + this.roomCode + ", code=" + this.code + ", language=" + this.language + ", updateType=" + this.updateType + ", timestamp=" + this.timestamp + ")";
        }
    }
}
