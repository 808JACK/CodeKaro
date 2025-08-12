/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class CodeStateDTO {
    private String roomCode;
    private String code;
    private String language;
    private Integer version;
    private Long timestamp;
    private Integer participantCount;

    @Generated
    public static CodeStateDTOBuilder builder() {
        return new CodeStateDTOBuilder();
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
    public Integer getVersion() {
        return this.version;
    }

    @Generated
    public Long getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public Integer getParticipantCount() {
        return this.participantCount;
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
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Generated
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CodeStateDTO)) {
            return false;
        }
        CodeStateDTO other = (CodeStateDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$version = this.getVersion();
        Integer other$version = other.getVersion();
        if (this$version == null ? other$version != null : !((Object)this$version).equals(other$version)) {
            return false;
        }
        Long this$timestamp = this.getTimestamp();
        Long other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        Integer this$participantCount = this.getParticipantCount();
        Integer other$participantCount = other.getParticipantCount();
        if (this$participantCount == null ? other$participantCount != null : !((Object)this$participantCount).equals(other$participantCount)) {
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
        return !(this$language == null ? other$language != null : !this$language.equals(other$language));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodeStateDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : ((Object)$version).hashCode());
        Long $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        Integer $participantCount = this.getParticipantCount();
        result = result * 59 + ($participantCount == null ? 43 : ((Object)$participantCount).hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CodeStateDTO(roomCode=" + this.getRoomCode() + ", code=" + this.getCode() + ", language=" + this.getLanguage() + ", version=" + this.getVersion() + ", timestamp=" + this.getTimestamp() + ", participantCount=" + this.getParticipantCount() + ")";
    }

    @Generated
    public CodeStateDTO() {
    }

    @Generated
    public CodeStateDTO(String roomCode, String code, String language, Integer version, Long timestamp, Integer participantCount) {
        this.roomCode = roomCode;
        this.code = code;
        this.language = language;
        this.version = version;
        this.timestamp = timestamp;
        this.participantCount = participantCount;
    }

    @Generated
    public static class CodeStateDTOBuilder {
        @Generated
        private String roomCode;
        @Generated
        private String code;
        @Generated
        private String language;
        @Generated
        private Integer version;
        @Generated
        private Long timestamp;
        @Generated
        private Integer participantCount;

        @Generated
        CodeStateDTOBuilder() {
        }

        @Generated
        public CodeStateDTOBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public CodeStateDTOBuilder code(String code) {
            this.code = code;
            return this;
        }

        @Generated
        public CodeStateDTOBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public CodeStateDTOBuilder version(Integer version) {
            this.version = version;
            return this;
        }

        @Generated
        public CodeStateDTOBuilder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public CodeStateDTOBuilder participantCount(Integer participantCount) {
            this.participantCount = participantCount;
            return this;
        }

        @Generated
        public CodeStateDTO build() {
            return new CodeStateDTO(this.roomCode, this.code, this.language, this.version, this.timestamp, this.participantCount);
        }

        @Generated
        public String toString() {
            return "CodeStateDTO.CodeStateDTOBuilder(roomCode=" + this.roomCode + ", code=" + this.code + ", language=" + this.language + ", version=" + this.version + ", timestamp=" + this.timestamp + ", participantCount=" + this.participantCount + ")";
        }
    }
}
