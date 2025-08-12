/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.CollectionTable
 *  jakarta.persistence.Column
 *  jakarta.persistence.ElementCollection
 *  jakarta.persistence.Entity
 *  jakarta.persistence.EnumType
 *  jakarta.persistence.Enumerated
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.JoinColumn
 *  jakarta.persistence.PrePersist
 *  jakarta.persistence.PreUpdate
 *  jakarta.persistence.Table
 *  lombok.Generated
 */
package com.example.demo.entities;

import com.example.demo.entities.ContestStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Generated;

@Entity
@Table(name="contests")
public class Contest {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="title", nullable=false)
    private String title;
    @Column(name="description", columnDefinition="TEXT")
    private String description;
    @Column(name="invite_link", unique=true, nullable=false)
    private String inviteLink;
    @Column(name="creator_id", nullable=false)
    private Long creatorId;
    @Column(name="start_time", nullable=false)
    private ZonedDateTime startTime;
    @Column(name="end_time", nullable=false)
    private ZonedDateTime endTime;
    @Column(name="duration_minutes", nullable=false)
    private Integer durationMinutes;
    @ElementCollection
    @CollectionTable(name="contest_problems", joinColumns={@JoinColumn(name="contest_id")})
    @Column(name="problem_id")
    private List<Long> problemIds;
    @Enumerated(value=EnumType.STRING)
    @Column(name="status", nullable=false)
    private ContestStatus status;
    @Column(name="max_participants")
    private Integer maxParticipants;
    @Column(name="is_public", nullable=false)
    private Boolean isPublic;
    @Column(name="created_at", nullable=false)
    private ZonedDateTime createdAt;
    @Column(name="updated_at")
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
        this.updatedAt = ZonedDateTime.now();
        if (this.status == null) {
            this.status = ContestStatus.UPCOMING;
        }
        if (this.isPublic == null) {
            this.isPublic = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    @Generated
    public static ContestBuilder builder() {
        return new ContestBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getInviteLink() {
        return this.inviteLink;
    }

    @Generated
    public Long getCreatorId() {
        return this.creatorId;
    }

    @Generated
    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    @Generated
    public ZonedDateTime getEndTime() {
        return this.endTime;
    }

    @Generated
    public Integer getDurationMinutes() {
        return this.durationMinutes;
    }

    @Generated
    public List<Long> getProblemIds() {
        return this.problemIds;
    }

    @Generated
    public ContestStatus getStatus() {
        return this.status;
    }

    @Generated
    public Integer getMaxParticipants() {
        return this.maxParticipants;
    }

    @Generated
    public Boolean getIsPublic() {
        return this.isPublic;
    }

    @Generated
    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setInviteLink(String inviteLink) {
        this.inviteLink = inviteLink;
    }

    @Generated
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @Generated
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    @Generated
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    @Generated
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Generated
    public void setProblemIds(List<Long> problemIds) {
        this.problemIds = problemIds;
    }

    @Generated
    public void setStatus(ContestStatus status) {
        this.status = status;
    }

    @Generated
    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    @Generated
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Generated
    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Contest)) {
            return false;
        }
        Contest other = (Contest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$creatorId = this.getCreatorId();
        Long other$creatorId = other.getCreatorId();
        if (this$creatorId == null ? other$creatorId != null : !((Object)this$creatorId).equals(other$creatorId)) {
            return false;
        }
        Integer this$durationMinutes = this.getDurationMinutes();
        Integer other$durationMinutes = other.getDurationMinutes();
        if (this$durationMinutes == null ? other$durationMinutes != null : !((Object)this$durationMinutes).equals(other$durationMinutes)) {
            return false;
        }
        Integer this$maxParticipants = this.getMaxParticipants();
        Integer other$maxParticipants = other.getMaxParticipants();
        if (this$maxParticipants == null ? other$maxParticipants != null : !((Object)this$maxParticipants).equals(other$maxParticipants)) {
            return false;
        }
        Boolean this$isPublic = this.getIsPublic();
        Boolean other$isPublic = other.getIsPublic();
        if (this$isPublic == null ? other$isPublic != null : !((Object)this$isPublic).equals(other$isPublic)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$inviteLink = this.getInviteLink();
        String other$inviteLink = other.getInviteLink();
        if (this$inviteLink == null ? other$inviteLink != null : !this$inviteLink.equals(other$inviteLink)) {
            return false;
        }
        ZonedDateTime this$startTime = this.getStartTime();
        ZonedDateTime other$startTime = other.getStartTime();
        if (this$startTime == null ? other$startTime != null : !((Object)this$startTime).equals(other$startTime)) {
            return false;
        }
        ZonedDateTime this$endTime = this.getEndTime();
        ZonedDateTime other$endTime = other.getEndTime();
        if (this$endTime == null ? other$endTime != null : !((Object)this$endTime).equals(other$endTime)) {
            return false;
        }
        List<Long> this$problemIds = this.getProblemIds();
        List<Long> other$problemIds = other.getProblemIds();
        if (this$problemIds == null ? other$problemIds != null : !((Object)this$problemIds).equals(other$problemIds)) {
            return false;
        }
        ContestStatus this$status = this.getStatus();
        ContestStatus other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)((Object)this$status)).equals((Object)other$status)) {
            return false;
        }
        ZonedDateTime this$createdAt = this.getCreatedAt();
        ZonedDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        ZonedDateTime this$updatedAt = this.getUpdatedAt();
        ZonedDateTime other$updatedAt = other.getUpdatedAt();
        return !(this$updatedAt == null ? other$updatedAt != null : !((Object)this$updatedAt).equals(other$updatedAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Contest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $creatorId = this.getCreatorId();
        result = result * 59 + ($creatorId == null ? 43 : ((Object)$creatorId).hashCode());
        Integer $durationMinutes = this.getDurationMinutes();
        result = result * 59 + ($durationMinutes == null ? 43 : ((Object)$durationMinutes).hashCode());
        Integer $maxParticipants = this.getMaxParticipants();
        result = result * 59 + ($maxParticipants == null ? 43 : ((Object)$maxParticipants).hashCode());
        Boolean $isPublic = this.getIsPublic();
        result = result * 59 + ($isPublic == null ? 43 : ((Object)$isPublic).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $inviteLink = this.getInviteLink();
        result = result * 59 + ($inviteLink == null ? 43 : $inviteLink.hashCode());
        ZonedDateTime $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        ZonedDateTime $endTime = this.getEndTime();
        result = result * 59 + ($endTime == null ? 43 : ((Object)$endTime).hashCode());
        List<Long> $problemIds = this.getProblemIds();
        result = result * 59 + ($problemIds == null ? 43 : ((Object)$problemIds).hashCode());
        ContestStatus $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)((Object)$status)).hashCode());
        ZonedDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        ZonedDateTime $updatedAt = this.getUpdatedAt();
        result = result * 59 + ($updatedAt == null ? 43 : ((Object)$updatedAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Contest(id=" + this.getId() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", inviteLink=" + this.getInviteLink() + ", creatorId=" + this.getCreatorId() + ", startTime=" + String.valueOf(this.getStartTime()) + ", endTime=" + String.valueOf(this.getEndTime()) + ", durationMinutes=" + this.getDurationMinutes() + ", problemIds=" + String.valueOf(this.getProblemIds()) + ", status=" + String.valueOf((Object)this.getStatus()) + ", maxParticipants=" + this.getMaxParticipants() + ", isPublic=" + this.getIsPublic() + ", createdAt=" + String.valueOf(this.getCreatedAt()) + ", updatedAt=" + String.valueOf(this.getUpdatedAt()) + ")";
    }

    @Generated
    public Contest() {
    }

    @Generated
    public Contest(Long id, String title, String description, String inviteLink, Long creatorId, ZonedDateTime startTime, ZonedDateTime endTime, Integer durationMinutes, List<Long> problemIds, ContestStatus status, Integer maxParticipants, Boolean isPublic, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.inviteLink = inviteLink;
        this.creatorId = creatorId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.problemIds = problemIds;
        this.status = status;
        this.maxParticipants = maxParticipants;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated
    public static class ContestBuilder {
        @Generated
        private Long id;
        @Generated
        private String title;
        @Generated
        private String description;
        @Generated
        private String inviteLink;
        @Generated
        private Long creatorId;
        @Generated
        private ZonedDateTime startTime;
        @Generated
        private ZonedDateTime endTime;
        @Generated
        private Integer durationMinutes;
        @Generated
        private List<Long> problemIds;
        @Generated
        private ContestStatus status;
        @Generated
        private Integer maxParticipants;
        @Generated
        private Boolean isPublic;
        @Generated
        private ZonedDateTime createdAt;
        @Generated
        private ZonedDateTime updatedAt;

        @Generated
        ContestBuilder() {
        }

        @Generated
        public ContestBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ContestBuilder title(String title) {
            this.title = title;
            return this;
        }

        @Generated
        public ContestBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public ContestBuilder inviteLink(String inviteLink) {
            this.inviteLink = inviteLink;
            return this;
        }

        @Generated
        public ContestBuilder creatorId(Long creatorId) {
            this.creatorId = creatorId;
            return this;
        }

        @Generated
        public ContestBuilder startTime(ZonedDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        @Generated
        public ContestBuilder endTime(ZonedDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        @Generated
        public ContestBuilder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }

        @Generated
        public ContestBuilder problemIds(List<Long> problemIds) {
            this.problemIds = problemIds;
            return this;
        }

        @Generated
        public ContestBuilder status(ContestStatus status) {
            this.status = status;
            return this;
        }

        @Generated
        public ContestBuilder maxParticipants(Integer maxParticipants) {
            this.maxParticipants = maxParticipants;
            return this;
        }

        @Generated
        public ContestBuilder isPublic(Boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        @Generated
        public ContestBuilder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public ContestBuilder updatedAt(ZonedDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Generated
        public Contest build() {
            return new Contest(this.id, this.title, this.description, this.inviteLink, this.creatorId, this.startTime, this.endTime, this.durationMinutes, this.problemIds, this.status, this.maxParticipants, this.isPublic, this.createdAt, this.updatedAt);
        }

        @Generated
        public String toString() {
            return "Contest.ContestBuilder(id=" + this.id + ", title=" + this.title + ", description=" + this.description + ", inviteLink=" + this.inviteLink + ", creatorId=" + this.creatorId + ", startTime=" + String.valueOf(this.startTime) + ", endTime=" + String.valueOf(this.endTime) + ", durationMinutes=" + this.durationMinutes + ", problemIds=" + String.valueOf(this.problemIds) + ", status=" + String.valueOf((Object)this.status) + ", maxParticipants=" + this.maxParticipants + ", isPublic=" + this.isPublic + ", createdAt=" + String.valueOf(this.createdAt) + ", updatedAt=" + String.valueOf(this.updatedAt) + ")";
        }
    }
}
