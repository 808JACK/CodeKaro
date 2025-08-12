/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.util.UUID;
import lombok.Generated;

public class OTOperationDTO {
    private UUID operationId;
    private Long userId;
    private String roomCode;
    private String operationType;
    private Integer position;
    private String chars;
    private Integer count;
    private Integer deleteCount;
    private Integer version;
    private Long timestamp;
    private String code;
    private String language;

    @Generated
    public static OTOperationDTOBuilder builder() {
        return new OTOperationDTOBuilder();
    }

    @Generated
    public UUID getOperationId() {
        return this.operationId;
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
    public String getOperationType() {
        return this.operationType;
    }

    @Generated
    public Integer getPosition() {
        return this.position;
    }

    @Generated
    public String getChars() {
        return this.chars;
    }

    @Generated
    public Integer getCount() {
        return this.count;
    }

    @Generated
    public Integer getDeleteCount() {
        return this.deleteCount;
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
    public String getCode() {
        return this.code;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public void setOperationId(UUID operationId) {
        this.operationId = operationId;
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
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Generated
    public void setPosition(Integer position) {
        this.position = position;
    }

    @Generated
    public void setChars(String chars) {
        this.chars = chars;
    }

    @Generated
    public void setCount(Integer count) {
        this.count = count;
    }

    @Generated
    public void setDeleteCount(Integer deleteCount) {
        this.deleteCount = deleteCount;
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
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OTOperationDTO)) {
            return false;
        }
        OTOperationDTO other = (OTOperationDTO)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$position = this.getPosition();
        Integer other$position = other.getPosition();
        if (this$position == null ? other$position != null : !((Object)this$position).equals(other$position)) {
            return false;
        }
        Integer this$count = this.getCount();
        Integer other$count = other.getCount();
        if (this$count == null ? other$count != null : !((Object)this$count).equals(other$count)) {
            return false;
        }
        Integer this$deleteCount = this.getDeleteCount();
        Integer other$deleteCount = other.getDeleteCount();
        if (this$deleteCount == null ? other$deleteCount != null : !((Object)this$deleteCount).equals(other$deleteCount)) {
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
        UUID this$operationId = this.getOperationId();
        UUID other$operationId = other.getOperationId();
        if (this$operationId == null ? other$operationId != null : !((Object)this$operationId).equals(other$operationId)) {
            return false;
        }
        String this$roomCode = this.getRoomCode();
        String other$roomCode = other.getRoomCode();
        if (this$roomCode == null ? other$roomCode != null : !this$roomCode.equals(other$roomCode)) {
            return false;
        }
        String this$operationType = this.getOperationType();
        String other$operationType = other.getOperationType();
        if (this$operationType == null ? other$operationType != null : !this$operationType.equals(other$operationType)) {
            return false;
        }
        String this$chars = this.getChars();
        String other$chars = other.getChars();
        if (this$chars == null ? other$chars != null : !this$chars.equals(other$chars)) {
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
        return other instanceof OTOperationDTO;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $position = this.getPosition();
        result = result * 59 + ($position == null ? 43 : ((Object)$position).hashCode());
        Integer $count = this.getCount();
        result = result * 59 + ($count == null ? 43 : ((Object)$count).hashCode());
        Integer $deleteCount = this.getDeleteCount();
        result = result * 59 + ($deleteCount == null ? 43 : ((Object)$deleteCount).hashCode());
        Integer $version = this.getVersion();
        result = result * 59 + ($version == null ? 43 : ((Object)$version).hashCode());
        Long $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        UUID $operationId = this.getOperationId();
        result = result * 59 + ($operationId == null ? 43 : ((Object)$operationId).hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        String $operationType = this.getOperationType();
        result = result * 59 + ($operationType == null ? 43 : $operationType.hashCode());
        String $chars = this.getChars();
        result = result * 59 + ($chars == null ? 43 : $chars.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "OTOperationDTO(operationId=" + String.valueOf(this.getOperationId()) + ", userId=" + this.getUserId() + ", roomCode=" + this.getRoomCode() + ", operationType=" + this.getOperationType() + ", position=" + this.getPosition() + ", chars=" + this.getChars() + ", count=" + this.getCount() + ", deleteCount=" + this.getDeleteCount() + ", version=" + this.getVersion() + ", timestamp=" + this.getTimestamp() + ", code=" + this.getCode() + ", language=" + this.getLanguage() + ")";
    }

    @Generated
    public OTOperationDTO() {
    }

    @Generated
    public OTOperationDTO(UUID operationId, Long userId, String roomCode, String operationType, Integer position, String chars, Integer count, Integer deleteCount, Integer version, Long timestamp, String code, String language) {
        this.operationId = operationId;
        this.userId = userId;
        this.roomCode = roomCode;
        this.operationType = operationType;
        this.position = position;
        this.chars = chars;
        this.count = count;
        this.deleteCount = deleteCount;
        this.version = version;
        this.timestamp = timestamp;
        this.code = code;
        this.language = language;
    }

    @Generated
    public static class OTOperationDTOBuilder {
        @Generated
        private UUID operationId;
        @Generated
        private Long userId;
        @Generated
        private String roomCode;
        @Generated
        private String operationType;
        @Generated
        private Integer position;
        @Generated
        private String chars;
        @Generated
        private Integer count;
        @Generated
        private Integer deleteCount;
        @Generated
        private Integer version;
        @Generated
        private Long timestamp;
        @Generated
        private String code;
        @Generated
        private String language;

        @Generated
        OTOperationDTOBuilder() {
        }

        @Generated
        public OTOperationDTOBuilder operationId(UUID operationId) {
            this.operationId = operationId;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder operationType(String operationType) {
            this.operationType = operationType;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder position(Integer position) {
            this.position = position;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder chars(String chars) {
            this.chars = chars;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder count(Integer count) {
            this.count = count;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder deleteCount(Integer deleteCount) {
            this.deleteCount = deleteCount;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder version(Integer version) {
            this.version = version;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder code(String code) {
            this.code = code;
            return this;
        }

        @Generated
        public OTOperationDTOBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public OTOperationDTO build() {
            return new OTOperationDTO(this.operationId, this.userId, this.roomCode, this.operationType, this.position, this.chars, this.count, this.deleteCount, this.version, this.timestamp, this.code, this.language);
        }

        @Generated
        public String toString() {
            return "OTOperationDTO.OTOperationDTOBuilder(operationId=" + String.valueOf(this.operationId) + ", userId=" + this.userId + ", roomCode=" + this.roomCode + ", operationType=" + this.operationType + ", position=" + this.position + ", chars=" + this.chars + ", count=" + this.count + ", deleteCount=" + this.deleteCount + ", version=" + this.version + ", timestamp=" + this.timestamp + ", code=" + this.code + ", language=" + this.language + ")";
        }
    }
}
