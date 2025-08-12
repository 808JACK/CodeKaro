/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Id
 *  lombok.Generated
 *  org.springframework.data.mongodb.core.mapping.Document
 */
package com.example.demo.entities;

import jakarta.persistence.Id;
import java.util.List;
import lombok.Generated;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="user_recent_rooms")
public class UserRecentRooms {
    @Id
    private Long userId;
    private List<Long> recentRoomIds;

    @Generated
    public static UserRecentRoomsBuilder builder() {
        return new UserRecentRoomsBuilder();
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public List<Long> getRecentRoomIds() {
        return this.recentRoomIds;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setRecentRoomIds(List<Long> recentRoomIds) {
        this.recentRoomIds = recentRoomIds;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UserRecentRooms)) {
            return false;
        }
        UserRecentRooms other = (UserRecentRooms)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        List<Long> this$recentRoomIds = this.getRecentRoomIds();
        List<Long> other$recentRoomIds = other.getRecentRoomIds();
        return !(this$recentRoomIds == null ? other$recentRoomIds != null : !((Object)this$recentRoomIds).equals(other$recentRoomIds));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof UserRecentRooms;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        List<Long> $recentRoomIds = this.getRecentRoomIds();
        result = result * 59 + ($recentRoomIds == null ? 43 : ((Object)$recentRoomIds).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "UserRecentRooms(userId=" + this.getUserId() + ", recentRoomIds=" + String.valueOf(this.getRecentRoomIds()) + ")";
    }

    @Generated
    public UserRecentRooms() {
    }

    @Generated
    public UserRecentRooms(Long userId, List<Long> recentRoomIds) {
        this.userId = userId;
        this.recentRoomIds = recentRoomIds;
    }

    @Generated
    public static class UserRecentRoomsBuilder {
        @Generated
        private Long userId;
        @Generated
        private List<Long> recentRoomIds;

        @Generated
        UserRecentRoomsBuilder() {
        }

        @Generated
        public UserRecentRoomsBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public UserRecentRoomsBuilder recentRoomIds(List<Long> recentRoomIds) {
            this.recentRoomIds = recentRoomIds;
            return this;
        }

        @Generated
        public UserRecentRooms build() {
            return new UserRecentRooms(this.userId, this.recentRoomIds);
        }

        @Generated
        public String toString() {
            return "UserRecentRooms.UserRecentRoomsBuilder(userId=" + this.userId + ", recentRoomIds=" + String.valueOf(this.recentRoomIds) + ")";
        }
    }
}
