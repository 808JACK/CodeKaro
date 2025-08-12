/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.EnumType
 *  jakarta.persistence.Enumerated
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.Table
 *  jakarta.persistence.UniqueConstraint
 *  lombok.Generated
 *  org.hibernate.annotations.CreationTimestamp
 */
package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.ZonedDateTime;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="room_participants", uniqueConstraints={@UniqueConstraint(columnNames={"room_id", "user_id"})})
public class RoomParticipants {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="room_id", nullable=false)
    private Long roomId;
    @Column(name="user_id", nullable=false)
    private Long userId;
    @Column(nullable=false)
    private String username;
    @Column(nullable=false)
    private Boolean isActive = true;
    @Enumerated(value=EnumType.STRING)
    @Column(nullable=false)
    private ParticipantRole role = ParticipantRole.PARTICIPANT;
    @CreationTimestamp
    @Column(updatable=false)
    private ZonedDateTime joinedAt;
    private ZonedDateTime lastSeenAt;

    @Generated
    public static RoomParticipantsBuilder builder() {
        return new RoomParticipantsBuilder();
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
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public String getUsername() {
        return this.username;
    }

    @Generated
    public Boolean getIsActive() {
        return this.isActive;
    }

    @Generated
    public ParticipantRole getRole() {
        return this.role;
    }

    @Generated
    public ZonedDateTime getJoinedAt() {
        return this.joinedAt;
    }

    @Generated
    public ZonedDateTime getLastSeenAt() {
        return this.lastSeenAt;
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
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setUsername(String username) {
        this.username = username;
    }

    @Generated
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Generated
    public void setRole(ParticipantRole role) {
        this.role = role;
    }

    @Generated
    public void setJoinedAt(ZonedDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Generated
    public void setLastSeenAt(ZonedDateTime lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoomParticipants)) {
            return false;
        }
        RoomParticipants other = (RoomParticipants)o;
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
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Boolean this$isActive = this.getIsActive();
        Boolean other$isActive = other.getIsActive();
        if (this$isActive == null ? other$isActive != null : !((Object)this$isActive).equals(other$isActive)) {
            return false;
        }
        String this$username = this.getUsername();
        String other$username = other.getUsername();
        if (this$username == null ? other$username != null : !this$username.equals(other$username)) {
            return false;
        }
        ParticipantRole this$role = this.getRole();
        ParticipantRole other$role = other.getRole();
        if (this$role == null ? other$role != null : !((Object)((Object)this$role)).equals((Object)other$role)) {
            return false;
        }
        ZonedDateTime this$joinedAt = this.getJoinedAt();
        ZonedDateTime other$joinedAt = other.getJoinedAt();
        if (this$joinedAt == null ? other$joinedAt != null : !((Object)this$joinedAt).equals(other$joinedAt)) {
            return false;
        }
        ZonedDateTime this$lastSeenAt = this.getLastSeenAt();
        ZonedDateTime other$lastSeenAt = other.getLastSeenAt();
        return !(this$lastSeenAt == null ? other$lastSeenAt != null : !((Object)this$lastSeenAt).equals(other$lastSeenAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RoomParticipants;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $roomId = this.getRoomId();
        result = result * 59 + ($roomId == null ? 43 : ((Object)$roomId).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Boolean $isActive = this.getIsActive();
        result = result * 59 + ($isActive == null ? 43 : ((Object)$isActive).hashCode());
        String $username = this.getUsername();
        result = result * 59 + ($username == null ? 43 : $username.hashCode());
        ParticipantRole $role = this.getRole();
        result = result * 59 + ($role == null ? 43 : ((Object)((Object)$role)).hashCode());
        ZonedDateTime $joinedAt = this.getJoinedAt();
        result = result * 59 + ($joinedAt == null ? 43 : ((Object)$joinedAt).hashCode());
        ZonedDateTime $lastSeenAt = this.getLastSeenAt();
        result = result * 59 + ($lastSeenAt == null ? 43 : ((Object)$lastSeenAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "RoomParticipants(id=" + this.getId() + ", roomId=" + this.getRoomId() + ", userId=" + this.getUserId() + ", username=" + this.getUsername() + ", isActive=" + this.getIsActive() + ", role=" + String.valueOf((Object)this.getRole()) + ", joinedAt=" + String.valueOf(this.getJoinedAt()) + ", lastSeenAt=" + String.valueOf(this.getLastSeenAt()) + ")";
    }

    @Generated
    public RoomParticipants() {
    }

    @Generated
    public RoomParticipants(Long id, Long roomId, Long userId, String username, Boolean isActive, ParticipantRole role, ZonedDateTime joinedAt, ZonedDateTime lastSeenAt) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.username = username;
        this.isActive = isActive;
        this.role = role;
        this.joinedAt = joinedAt;
        this.lastSeenAt = lastSeenAt;
    }

    @Generated
    public static class RoomParticipantsBuilder {
        @Generated
        private Long id;
        @Generated
        private Long roomId;
        @Generated
        private Long userId;
        @Generated
        private String username;
        @Generated
        private Boolean isActive;
        @Generated
        private ParticipantRole role;
        @Generated
        private ZonedDateTime joinedAt;
        @Generated
        private ZonedDateTime lastSeenAt;

        @Generated
        RoomParticipantsBuilder() {
        }

        @Generated
        public RoomParticipantsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public RoomParticipantsBuilder roomId(Long roomId) {
            this.roomId = roomId;
            return this;
        }

        @Generated
        public RoomParticipantsBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public RoomParticipantsBuilder username(String username) {
            this.username = username;
            return this;
        }

        @Generated
        public RoomParticipantsBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        @Generated
        public RoomParticipantsBuilder role(ParticipantRole role) {
            this.role = role;
            return this;
        }

        @Generated
        public RoomParticipantsBuilder joinedAt(ZonedDateTime joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        @Generated
        public RoomParticipantsBuilder lastSeenAt(ZonedDateTime lastSeenAt) {
            this.lastSeenAt = lastSeenAt;
            return this;
        }

        @Generated
        public RoomParticipants build() {
            return new RoomParticipants(this.id, this.roomId, this.userId, this.username, this.isActive, this.role, this.joinedAt, this.lastSeenAt);
        }

        @Generated
        public String toString() {
            return "RoomParticipants.RoomParticipantsBuilder(id=" + this.id + ", roomId=" + this.roomId + ", userId=" + this.userId + ", username=" + this.username + ", isActive=" + this.isActive + ", role=" + String.valueOf((Object)this.role) + ", joinedAt=" + String.valueOf(this.joinedAt) + ", lastSeenAt=" + String.valueOf(this.lastSeenAt) + ")";
        }
    }

    public static enum ParticipantRole {
        CREATOR,
        PARTICIPANT;

    }
}
