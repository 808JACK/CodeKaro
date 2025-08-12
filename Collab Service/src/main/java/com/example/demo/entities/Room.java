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
 *  jakarta.persistence.FetchType
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Index
 *  jakarta.persistence.JoinColumn
 *  jakarta.persistence.Table
 *  lombok.Generated
 *  org.hibernate.annotations.CreationTimestamp
 *  org.hibernate.annotations.UpdateTimestamp
 */
package com.example.demo.entities;

import com.example.demo.entities.RoomStatus;
import com.example.demo.entities.RoomType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Builder
@Getter
@Setter
@Table(name="rooms", indexes={@Index(name="idx_room_invite_code", columnList="inviteCode", unique=true)})
public class Room {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true, updatable=false)
    private String inviteCode;
    private String name;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private RoomStatus status = RoomStatus.ACTIVE;
    @Column(nullable=false)
    private Long creatorUserId;
    @Column(nullable=false)
    private Boolean voiceEnabled;
    @Enumerated(value=EnumType.STRING)
    @Column(name="room_type")
    private RoomType roomType = RoomType.COLLAB;
    @ElementCollection(fetch=FetchType.LAZY)
    @CollectionTable(name="room_problems", joinColumns={@JoinColumn(name="room_id")})
    @Column(name="problem_id")
    private List<Long> problemIds;
    @CreationTimestamp
    @Column(updatable=false)
    private ZonedDateTime createdAt;
    @UpdateTimestamp
    private ZonedDateTime lastActiveAt;

    @Generated
    public static RoomBuilder builder() {
        return new RoomBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getInviteCode() {
        return this.inviteCode;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public RoomStatus getStatus() {
        return this.status;
    }

    @Generated
    public Long getCreatorUserId() {
        return this.creatorUserId;
    }

    @Generated
    public Boolean getVoiceEnabled() {
        return this.voiceEnabled;
    }

    @Generated
    public RoomType getRoomType() {
        return this.roomType;
    }

    @Generated
    public List<Long> getProblemIds() {
        return this.problemIds;
    }

    @Generated
    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public ZonedDateTime getLastActiveAt() {
        return this.lastActiveAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Generated
    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    @Generated
    public void setVoiceEnabled(Boolean voiceEnabled) {
        this.voiceEnabled = voiceEnabled;
    }

    @Generated
    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    @Generated
    public void setProblemIds(List<Long> problemIds) {
        this.problemIds = problemIds;
    }

    @Generated
    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Generated
    public void setLastActiveAt(ZonedDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room other = (Room)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$creatorUserId = this.getCreatorUserId();
        Long other$creatorUserId = other.getCreatorUserId();
        if (this$creatorUserId == null ? other$creatorUserId != null : !((Object)this$creatorUserId).equals(other$creatorUserId)) {
            return false;
        }
        Boolean this$voiceEnabled = this.getVoiceEnabled();
        Boolean other$voiceEnabled = other.getVoiceEnabled();
        if (this$voiceEnabled == null ? other$voiceEnabled != null : !((Object)this$voiceEnabled).equals(other$voiceEnabled)) {
            return false;
        }
        String this$inviteCode = this.getInviteCode();
        String other$inviteCode = other.getInviteCode();
        if (this$inviteCode == null ? other$inviteCode != null : !this$inviteCode.equals(other$inviteCode)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        RoomStatus this$status = this.getStatus();
        RoomStatus other$status = other.getStatus();
        if (this$status == null ? other$status != null : !((Object)((Object)this$status)).equals((Object)other$status)) {
            return false;
        }
        RoomType this$roomType = this.getRoomType();
        RoomType other$roomType = other.getRoomType();
        if (this$roomType == null ? other$roomType != null : !((Object)((Object)this$roomType)).equals((Object)other$roomType)) {
            return false;
        }
        List<Long> this$problemIds = this.getProblemIds();
        List<Long> other$problemIds = other.getProblemIds();
        if (this$problemIds == null ? other$problemIds != null : !((Object)this$problemIds).equals(other$problemIds)) {
            return false;
        }
        ZonedDateTime this$createdAt = this.getCreatedAt();
        ZonedDateTime other$createdAt = other.getCreatedAt();
        if (this$createdAt == null ? other$createdAt != null : !((Object)this$createdAt).equals(other$createdAt)) {
            return false;
        }
        ZonedDateTime this$lastActiveAt = this.getLastActiveAt();
        ZonedDateTime other$lastActiveAt = other.getLastActiveAt();
        return !(this$lastActiveAt == null ? other$lastActiveAt != null : !((Object)this$lastActiveAt).equals(other$lastActiveAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof Room;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $creatorUserId = this.getCreatorUserId();
        result = result * 59 + ($creatorUserId == null ? 43 : ((Object)$creatorUserId).hashCode());
        Boolean $voiceEnabled = this.getVoiceEnabled();
        result = result * 59 + ($voiceEnabled == null ? 43 : ((Object)$voiceEnabled).hashCode());
        String $inviteCode = this.getInviteCode();
        result = result * 59 + ($inviteCode == null ? 43 : $inviteCode.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        RoomStatus $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : ((Object)((Object)$status)).hashCode());
        RoomType $roomType = this.getRoomType();
        result = result * 59 + ($roomType == null ? 43 : ((Object)((Object)$roomType)).hashCode());
        List<Long> $problemIds = this.getProblemIds();
        result = result * 59 + ($problemIds == null ? 43 : ((Object)$problemIds).hashCode());
        ZonedDateTime $createdAt = this.getCreatedAt();
        result = result * 59 + ($createdAt == null ? 43 : ((Object)$createdAt).hashCode());
        ZonedDateTime $lastActiveAt = this.getLastActiveAt();
        result = result * 59 + ($lastActiveAt == null ? 43 : ((Object)$lastActiveAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "Room(id=" + this.getId() + ", inviteCode=" + this.getInviteCode() + ", name=" + this.getName() + ", status=" + String.valueOf((Object)this.getStatus()) + ", creatorUserId=" + this.getCreatorUserId() + ", voiceEnabled=" + this.getVoiceEnabled() + ", roomType=" + String.valueOf((Object)this.getRoomType()) + ", problemIds=" + String.valueOf(this.getProblemIds()) + ", createdAt=" + String.valueOf(this.getCreatedAt()) + ", lastActiveAt=" + String.valueOf(this.getLastActiveAt()) + ")";
    }

    @Generated
    public Room() {
    }

    @Generated
    public Room(Long id, String inviteCode, String name, RoomStatus status, Long creatorUserId, Boolean voiceEnabled, RoomType roomType, List<Long> problemIds, ZonedDateTime createdAt, ZonedDateTime lastActiveAt) {
        this.id = id;
        this.inviteCode = inviteCode;
        this.name = name;
        this.status = status;
        this.creatorUserId = creatorUserId;
        this.voiceEnabled = voiceEnabled;
        this.roomType = roomType;
        this.problemIds = problemIds;
        this.createdAt = createdAt;
        this.lastActiveAt = lastActiveAt;
    }

    @Generated
    public static class RoomBuilder {
        @Generated
        private Long id;
        @Generated
        private String inviteCode;
        @Generated
        private String name;
        @Generated
        private RoomStatus status;
        @Generated
        private Long creatorUserId;
        @Generated
        private Boolean voiceEnabled;
        @Generated
        private RoomType roomType;
        @Generated
        private List<Long> problemIds;
        @Generated
        private ZonedDateTime createdAt;
        @Generated
        private ZonedDateTime lastActiveAt;

        @Generated
        RoomBuilder() {
        }

        @Generated
        public RoomBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public RoomBuilder inviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
            return this;
        }

        @Generated
        public RoomBuilder name(String name) {
            this.name = name;
            return this;
        }

        @Generated
        public RoomBuilder status(RoomStatus status) {
            this.status = status;
            return this;
        }

        @Generated
        public RoomBuilder creatorUserId(Long creatorUserId) {
            this.creatorUserId = creatorUserId;
            return this;
        }

        @Generated
        public RoomBuilder voiceEnabled(Boolean voiceEnabled) {
            this.voiceEnabled = voiceEnabled;
            return this;
        }

        @Generated
        public RoomBuilder roomType(RoomType roomType) {
            this.roomType = roomType;
            return this;
        }

        @Generated
        public RoomBuilder problemIds(List<Long> problemIds) {
            this.problemIds = problemIds;
            return this;
        }

        @Generated
        public RoomBuilder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        @Generated
        public RoomBuilder lastActiveAt(ZonedDateTime lastActiveAt) {
            this.lastActiveAt = lastActiveAt;
            return this;
        }

        @Generated
        public Room build() {
            return new Room(this.id, this.inviteCode, this.name, this.status, this.creatorUserId, this.voiceEnabled, this.roomType, this.problemIds, this.createdAt, this.lastActiveAt);
        }

        @Generated
        public String toString() {
            return "Room.RoomBuilder(id=" + this.id + ", inviteCode=" + this.inviteCode + ", name=" + this.name + ", status=" + String.valueOf((Object)this.status) + ", creatorUserId=" + this.creatorUserId + ", voiceEnabled=" + this.voiceEnabled + ", roomType=" + String.valueOf((Object)this.roomType) + ", problemIds=" + String.valueOf(this.problemIds) + ", createdAt=" + String.valueOf(this.createdAt) + ", lastActiveAt=" + String.valueOf(this.lastActiveAt) + ")";
        }
    }
}
