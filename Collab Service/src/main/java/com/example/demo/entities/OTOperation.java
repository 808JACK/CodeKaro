/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.entities;

import java.time.Instant;
import java.util.UUID;
import lombok.Generated;

public class OTOperation {
    private Long roomId;
    private UUID operationId;
    private Long userId;
    private String operationType;
    private Integer position;
    private String chars;
    private Integer count;
    private Integer deleteCount;
    private Integer version;
    private Instant timestamp;

    @Generated
    public static OTOperationBuilder builder() {
        return new OTOperationBuilder();
    }

    @Generated
    public Long getRoomId() {
        return this.roomId;
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
    public Instant getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof OTOperation)) {
            return false;
        }
        OTOperation other = (OTOperation)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$roomId = this.getRoomId();
        Long other$roomId = other.getRoomId();
        if (this$roomId == null ? other$roomId != null : !((Object)this$roomId).equals(other$roomId)) {
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
        UUID this$operationId = this.getOperationId();
        UUID other$operationId = other.getOperationId();
        if (this$operationId == null ? other$operationId != null : !((Object)this$operationId).equals(other$operationId)) {
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
        Instant this$timestamp = this.getTimestamp();
        Instant other$timestamp = other.getTimestamp();
        return !(this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof OTOperation;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $roomId = this.getRoomId();
        result = result * 59 + ($roomId == null ? 43 : ((Object)$roomId).hashCode());
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
        UUID $operationId = this.getOperationId();
        result = result * 59 + ($operationId == null ? 43 : ((Object)$operationId).hashCode());
        String $operationType = this.getOperationType();
        result = result * 59 + ($operationType == null ? 43 : $operationType.hashCode());
        String $chars = this.getChars();
        result = result * 59 + ($chars == null ? 43 : $chars.hashCode());
        Instant $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "OTOperation(roomId=" + this.getRoomId() + ", operationId=" + String.valueOf(this.getOperationId()) + ", userId=" + this.getUserId() + ", operationType=" + this.getOperationType() + ", position=" + this.getPosition() + ", chars=" + this.getChars() + ", count=" + this.getCount() + ", deleteCount=" + this.getDeleteCount() + ", version=" + this.getVersion() + ", timestamp=" + String.valueOf(this.getTimestamp()) + ")";
    }

    @Generated
    public OTOperation() {
    }

    @Generated
    public OTOperation(Long roomId, UUID operationId, Long userId, String operationType, Integer position, String chars, Integer count, Integer deleteCount, Integer version, Instant timestamp) {
        this.roomId = roomId;
        this.operationId = operationId;
        this.userId = userId;
        this.operationType = operationType;
        this.position = position;
        this.chars = chars;
        this.count = count;
        this.deleteCount = deleteCount;
        this.version = version;
        this.timestamp = timestamp;
    }

    @Generated
    public static class OTOperationBuilder {
        @Generated
        private Long roomId;
        @Generated
        private UUID operationId;
        @Generated
        private Long userId;
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
        private Instant timestamp;

        @Generated
        OTOperationBuilder() {
        }

        @Generated
        public OTOperationBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        @Generated
        public OTOperationBuilder operationId(UUID operationId) {
            this.operationId = operationId;
            return this;
        }

        @Generated
        public OTOperationBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public OTOperationBuilder operationType(String operationType) {
            this.operationType = operationType;
            return this;
        }

        @Generated
        public OTOperationBuilder position(Integer position) {
            this.position = position;
            return this;
        }

        @Generated
        public OTOperationBuilder chars(String chars) {
            this.chars = chars;
            return this;
        }

        @Generated
        public OTOperationBuilder count(Integer count) {
            this.count = count;
            return this;
        }

        @Generated
        public OTOperationBuilder deleteCount(Integer deleteCount) {
            this.deleteCount = deleteCount;
            return this;
        }

        @Generated
        public OTOperationBuilder version(Integer version) {
            this.version = version;
            return this;
        }

        @Generated
        public OTOperationBuilder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public OTOperation build() {
            return new OTOperation(this.roomId, this.operationId, this.userId, this.operationType, this.position, this.chars, this.count, this.deleteCount, this.version, this.timestamp);
        }

        @Generated
        public String toString() {
            return "OTOperation.OTOperationBuilder(roomId=" + this.roomId + ", operationId=" + String.valueOf(this.operationId) + ", userId=" + this.userId + ", operationType=" + this.operationType + ", position=" + this.position + ", chars=" + this.chars + ", count=" + this.count + ", deleteCount=" + this.deleteCount + ", version=" + this.version + ", timestamp=" + String.valueOf(this.timestamp) + ")";
        }
    }
}
