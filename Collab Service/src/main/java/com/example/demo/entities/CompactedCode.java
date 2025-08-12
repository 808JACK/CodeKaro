/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.springframework.data.annotation.Id
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.example.demo.entities;

import java.time.Instant;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="room_compacted_code_states")
public class CompactedCode {
    @Id
    private String id;
    private Long roomId;
    private Long problemId;
    private String code;
    private Instant lastCompactedAt;

    @Generated
    public static CompactedCodeBuilder builder() {
        return new CompactedCodeBuilder();
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public Long getRoomId() {
        return this.roomId;
    }

    @Generated
    public Long getProblemId() {
        return this.problemId;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public Instant getLastCompactedAt() {
        return this.lastCompactedAt;
    }

    @Generated
    public void setId(String id) {
        this.id = id;
    }

    @Generated
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Generated
    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    @Generated
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setLastCompactedAt(Instant lastCompactedAt) {
        this.lastCompactedAt = lastCompactedAt;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CompactedCode)) {
            return false;
        }
        CompactedCode other = (CompactedCode)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$roomId = this.getRoomId();
        Long other$roomId = other.getRoomId();
        if (this$roomId == null ? other$roomId != null : !((Object)this$roomId).equals(other$roomId)) {
            return false;
        }
        Long this$problemId = this.getProblemId();
        Long other$problemId = other.getProblemId();
        if (this$problemId == null ? other$problemId != null : !((Object)this$problemId).equals(other$problemId)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        Instant this$lastCompactedAt = this.getLastCompactedAt();
        Instant other$lastCompactedAt = other.getLastCompactedAt();
        return !(this$lastCompactedAt == null ? other$lastCompactedAt != null : !((Object)this$lastCompactedAt).equals(other$lastCompactedAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CompactedCode;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $roomId = this.getRoomId();
        result = result * 59 + ($roomId == null ? 43 : ((Object)$roomId).hashCode());
        Long $problemId = this.getProblemId();
        result = result * 59 + ($problemId == null ? 43 : ((Object)$problemId).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Instant $lastCompactedAt = this.getLastCompactedAt();
        result = result * 59 + ($lastCompactedAt == null ? 43 : ((Object)$lastCompactedAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CompactedCode(id=" + this.getId() + ", roomId=" + this.getRoomId() + ", problemId=" + this.getProblemId() + ", code=" + this.getCode() + ", lastCompactedAt=" + String.valueOf(this.getLastCompactedAt()) + ")";
    }

    @Generated
    public CompactedCode() {
    }

    @Generated
    public CompactedCode(String id, Long roomId, Long problemId, String code, Instant lastCompactedAt) {
        this.id = id;
        this.roomId = roomId;
        this.problemId = problemId;
        this.code = code;
        this.lastCompactedAt = lastCompactedAt;
    }

    @Generated
    public static class CompactedCodeBuilder {
        @Generated
        private String id;
        @Generated
        private Long roomId;
        @Generated
        private Long problemId;
        @Generated
        private String code;
        @Generated
        private Instant lastCompactedAt;

        @Generated
        CompactedCodeBuilder() {
        }

        @Generated
        public CompactedCodeBuilder id(String id) {
            this.id = id;
            return this;
        }

        @Generated
        public CompactedCodeBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        @Generated
        public CompactedCodeBuilder problemId(Long problemId) {
            this.problemId = problemId;
            return this;
        }

        @Generated
        public CompactedCodeBuilder code(String code) {
            this.code = code;
            return this;
        }

        @Generated
        public CompactedCodeBuilder lastCompactedAt(Instant lastCompactedAt) {
            this.lastCompactedAt = lastCompactedAt;
            return this;
        }

        @Generated
        public CompactedCode build() {
            return new CompactedCode(this.id, this.roomId, this.problemId, this.code, this.lastCompactedAt);
        }

        @Generated
        public String toString() {
            return "CompactedCode.CompactedCodeBuilder(id=" + this.id + ", roomId=" + this.roomId + ", problemId=" + this.problemId + ", code=" + this.code + ", lastCompactedAt=" + String.valueOf(this.lastCompactedAt) + ")";
        }
    }
}
