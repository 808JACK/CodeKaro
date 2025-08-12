/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Table
 *  jakarta.persistence.Transient
 *  jakarta.persistence.UniqueConstraint
 *  lombok.Generated
 *  org.hibernate.annotations.UpdateTimestamp
 */
package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Generated;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="room_code_states", uniqueConstraints={@UniqueConstraint(columnNames={"room_id"})})
public class RoomCodeState {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="room_id", nullable=false, unique=true)
    private Long roomId;
    @Column(name="problem_id", nullable=true)
    private Long problemId;
    @Column(name="current_code_doc_id")
    private String currentCodeDocId;
    @Column(name="final_code", columnDefinition="TEXT")
    private String finalCode;
    @Column(name="language")
    private String language;
    @Column(name="operation_count")
    private Integer operationCount;
    @Column(name="compaction_timestamp")
    private ZonedDateTime compactionTimestamp;
    @Column(name="current_version")
    private Integer currentVersion = 0;
    @UpdateTimestamp
    private ZonedDateTime lastUpdatedAt;
    @Column(name="metadata", columnDefinition="TEXT")
    private String metadataJson;
    @Transient
    private Map<String, Object> metadata;

    public Map<String, Object> getMetadata() {
        if (this.metadata == null) {
            this.metadata = new HashMap<String, Object>();
        }
        return this.metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Generated
    public static RoomCodeStateBuilder builder() {
        return new RoomCodeStateBuilder();
    }

    @Generated
    public Long getId() {
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
    public String getCurrentCodeDocId() {
        return this.currentCodeDocId;
    }

    @Generated
    public String getFinalCode() {
        return this.finalCode;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public Integer getOperationCount() {
        return this.operationCount;
    }

    @Generated
    public ZonedDateTime getCompactionTimestamp() {
        return this.compactionTimestamp;
    }

    @Generated
    public Integer getCurrentVersion() {
        return this.currentVersion;
    }

    @Generated
    public ZonedDateTime getLastUpdatedAt() {
        return this.lastUpdatedAt;
    }

    @Generated
    public String getMetadataJson() {
        return this.metadataJson;
    }

    @Generated
    public void setId(Long id) {
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
    public void setCurrentCodeDocId(String currentCodeDocId) {
        this.currentCodeDocId = currentCodeDocId;
    }

    @Generated
    public void setFinalCode(String finalCode) {
        this.finalCode = finalCode;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setOperationCount(Integer operationCount) {
        this.operationCount = operationCount;
    }

    @Generated
    public void setCompactionTimestamp(ZonedDateTime compactionTimestamp) {
        this.compactionTimestamp = compactionTimestamp;
    }

    @Generated
    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    @Generated
    public void setLastUpdatedAt(ZonedDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Generated
    public void setMetadataJson(String metadataJson) {
        this.metadataJson = metadataJson;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoomCodeState)) {
            return false;
        }
        RoomCodeState other = (RoomCodeState)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
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
        Integer this$operationCount = this.getOperationCount();
        Integer other$operationCount = other.getOperationCount();
        if (this$operationCount == null ? other$operationCount != null : !((Object)this$operationCount).equals(other$operationCount)) {
            return false;
        }
        Integer this$currentVersion = this.getCurrentVersion();
        Integer other$currentVersion = other.getCurrentVersion();
        if (this$currentVersion == null ? other$currentVersion != null : !((Object)this$currentVersion).equals(other$currentVersion)) {
            return false;
        }
        String this$currentCodeDocId = this.getCurrentCodeDocId();
        String other$currentCodeDocId = other.getCurrentCodeDocId();
        if (this$currentCodeDocId == null ? other$currentCodeDocId != null : !this$currentCodeDocId.equals(other$currentCodeDocId)) {
            return false;
        }
        String this$finalCode = this.getFinalCode();
        String other$finalCode = other.getFinalCode();
        if (this$finalCode == null ? other$finalCode != null : !this$finalCode.equals(other$finalCode)) {
            return false;
        }
        String this$language = this.getLanguage();
        String other$language = other.getLanguage();
        if (this$language == null ? other$language != null : !this$language.equals(other$language)) {
            return false;
        }
        ZonedDateTime this$compactionTimestamp = this.getCompactionTimestamp();
        ZonedDateTime other$compactionTimestamp = other.getCompactionTimestamp();
        if (this$compactionTimestamp == null ? other$compactionTimestamp != null : !((Object)this$compactionTimestamp).equals(other$compactionTimestamp)) {
            return false;
        }
        ZonedDateTime this$lastUpdatedAt = this.getLastUpdatedAt();
        ZonedDateTime other$lastUpdatedAt = other.getLastUpdatedAt();
        if (this$lastUpdatedAt == null ? other$lastUpdatedAt != null : !((Object)this$lastUpdatedAt).equals(other$lastUpdatedAt)) {
            return false;
        }
        String this$metadataJson = this.getMetadataJson();
        String other$metadataJson = other.getMetadataJson();
        if (this$metadataJson == null ? other$metadataJson != null : !this$metadataJson.equals(other$metadataJson)) {
            return false;
        }
        Map<String, Object> this$metadata = this.getMetadata();
        Map<String, Object> other$metadata = other.getMetadata();
        return !(this$metadata == null ? other$metadata != null : !((Object)this$metadata).equals(other$metadata));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RoomCodeState;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $roomId = this.getRoomId();
        result = result * 59 + ($roomId == null ? 43 : ((Object)$roomId).hashCode());
        Long $problemId = this.getProblemId();
        result = result * 59 + ($problemId == null ? 43 : ((Object)$problemId).hashCode());
        Integer $operationCount = this.getOperationCount();
        result = result * 59 + ($operationCount == null ? 43 : ((Object)$operationCount).hashCode());
        Integer $currentVersion = this.getCurrentVersion();
        result = result * 59 + ($currentVersion == null ? 43 : ((Object)$currentVersion).hashCode());
        String $currentCodeDocId = this.getCurrentCodeDocId();
        result = result * 59 + ($currentCodeDocId == null ? 43 : $currentCodeDocId.hashCode());
        String $finalCode = this.getFinalCode();
        result = result * 59 + ($finalCode == null ? 43 : $finalCode.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        ZonedDateTime $compactionTimestamp = this.getCompactionTimestamp();
        result = result * 59 + ($compactionTimestamp == null ? 43 : ((Object)$compactionTimestamp).hashCode());
        ZonedDateTime $lastUpdatedAt = this.getLastUpdatedAt();
        result = result * 59 + ($lastUpdatedAt == null ? 43 : ((Object)$lastUpdatedAt).hashCode());
        String $metadataJson = this.getMetadataJson();
        result = result * 59 + ($metadataJson == null ? 43 : $metadataJson.hashCode());
        Map<String, Object> $metadata = this.getMetadata();
        result = result * 59 + ($metadata == null ? 43 : ((Object)$metadata).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "RoomCodeState(id=" + this.getId() + ", roomId=" + this.getRoomId() + ", problemId=" + this.getProblemId() + ", currentCodeDocId=" + this.getCurrentCodeDocId() + ", finalCode=" + this.getFinalCode() + ", language=" + this.getLanguage() + ", operationCount=" + this.getOperationCount() + ", compactionTimestamp=" + String.valueOf(this.getCompactionTimestamp()) + ", currentVersion=" + this.getCurrentVersion() + ", lastUpdatedAt=" + String.valueOf(this.getLastUpdatedAt()) + ", metadataJson=" + this.getMetadataJson() + ", metadata=" + String.valueOf(this.getMetadata()) + ")";
    }

    @Generated
    public RoomCodeState() {
    }

    @Generated
    public RoomCodeState(Long id, Long roomId, Long problemId, String currentCodeDocId, String finalCode, String language, Integer operationCount, ZonedDateTime compactionTimestamp, Integer currentVersion, ZonedDateTime lastUpdatedAt, String metadataJson, Map<String, Object> metadata) {
        this.id = id;
        this.roomId = roomId;
        this.problemId = problemId;
        this.currentCodeDocId = currentCodeDocId;
        this.finalCode = finalCode;
        this.language = language;
        this.operationCount = operationCount;
        this.compactionTimestamp = compactionTimestamp;
        this.currentVersion = currentVersion;
        this.lastUpdatedAt = lastUpdatedAt;
        this.metadataJson = metadataJson;
        this.metadata = metadata;
    }

    @Generated
    public static class RoomCodeStateBuilder {
        @Generated
        private Long id;
        @Generated
        private Long roomId;
        @Generated
        private Long problemId;
        @Generated
        private String currentCodeDocId;
        @Generated
        private String finalCode;
        @Generated
        private String language;
        @Generated
        private Integer operationCount;
        @Generated
        private ZonedDateTime compactionTimestamp;
        @Generated
        private Integer currentVersion;
        @Generated
        private ZonedDateTime lastUpdatedAt;
        @Generated
        private String metadataJson;
        @Generated
        private Map<String, Object> metadata;

        @Generated
        RoomCodeStateBuilder() {
        }

        @Generated
        public RoomCodeStateBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder problemId(Long problemId) {
            this.problemId = problemId;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder currentCodeDocId(String currentCodeDocId) {
            this.currentCodeDocId = currentCodeDocId;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder finalCode(String finalCode) {
            this.finalCode = finalCode;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder operationCount(Integer operationCount) {
            this.operationCount = operationCount;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder compactionTimestamp(ZonedDateTime compactionTimestamp) {
            this.compactionTimestamp = compactionTimestamp;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder currentVersion(Integer currentVersion) {
            this.currentVersion = currentVersion;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder lastUpdatedAt(ZonedDateTime lastUpdatedAt) {
            this.lastUpdatedAt = lastUpdatedAt;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder metadataJson(String metadataJson) {
            this.metadataJson = metadataJson;
            return this;
        }

        @Generated
        public RoomCodeStateBuilder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        @Generated
        public RoomCodeState build() {
            return new RoomCodeState(this.id, this.roomId, this.problemId, this.currentCodeDocId, this.finalCode, this.language, this.operationCount, this.compactionTimestamp, this.currentVersion, this.lastUpdatedAt, this.metadataJson, this.metadata);
        }

        @Generated
        public String toString() {
            return "RoomCodeState.RoomCodeStateBuilder(id=" + this.id + ", roomId=" + this.roomId + ", problemId=" + this.problemId + ", currentCodeDocId=" + this.currentCodeDocId + ", finalCode=" + this.finalCode + ", language=" + this.language + ", operationCount=" + this.operationCount + ", compactionTimestamp=" + String.valueOf(this.compactionTimestamp) + ", currentVersion=" + this.currentVersion + ", lastUpdatedAt=" + String.valueOf(this.lastUpdatedAt) + ", metadataJson=" + this.metadataJson + ", metadata=" + String.valueOf(this.metadata) + ")";
        }
    }
}
