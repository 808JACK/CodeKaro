/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.util.Set;
import lombok.Generated;

public class CreateRoomRequest {
    private String name;
    private Set<Long> problemIds;
    private boolean voiceEnabled;
    private String roomType;

    @Generated
    public CreateRoomRequest() {
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public Set<Long> getProblemIds() {
        return this.problemIds;
    }

    @Generated
    public boolean isVoiceEnabled() {
        return this.voiceEnabled;
    }

    @Generated
    public String getRoomType() {
        return this.roomType;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setProblemIds(Set<Long> problemIds) {
        this.problemIds = problemIds;
    }

    @Generated
    public void setVoiceEnabled(boolean voiceEnabled) {
        this.voiceEnabled = voiceEnabled;
    }

    @Generated
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CreateRoomRequest)) {
            return false;
        }
        CreateRoomRequest other = (CreateRoomRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isVoiceEnabled() != other.isVoiceEnabled()) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        Set<Long> this$problemIds = this.getProblemIds();
        Set<Long> other$problemIds = other.getProblemIds();
        if (this$problemIds == null ? other$problemIds != null : !((Object)this$problemIds).equals(other$problemIds)) {
            return false;
        }
        String this$roomType = this.getRoomType();
        String other$roomType = other.getRoomType();
        return !(this$roomType == null ? other$roomType != null : !this$roomType.equals(other$roomType));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CreateRoomRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isVoiceEnabled() ? 79 : 97);
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Set<Long> $problemIds = this.getProblemIds();
        result = result * 59 + ($problemIds == null ? 43 : ((Object)$problemIds).hashCode());
        String $roomType = this.getRoomType();
        result = result * 59 + ($roomType == null ? 43 : $roomType.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CreateRoomRequest(name=" + this.getName() + ", problemIds=" + String.valueOf(this.getProblemIds()) + ", voiceEnabled=" + this.isVoiceEnabled() + ", roomType=" + this.getRoomType() + ")";
    }
}
