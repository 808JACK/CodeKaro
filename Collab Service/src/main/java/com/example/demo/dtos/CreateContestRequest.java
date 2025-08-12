/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.util.List;
import lombok.Generated;

public class CreateContestRequest {
    private String name;
    private String description;
    private List<Long> problemIds;
    private Integer durationMinutes;
    private Integer maxParticipants;
    private Boolean isPublic;

    @Generated
    public static CreateContestRequestBuilder builder() {
        return new CreateContestRequestBuilder();
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getDescription() {
        return this.description;
    }

    @Generated
    public List<Long> getProblemIds() {
        return this.problemIds;
    }

    @Generated
    public Integer getDurationMinutes() {
        return this.durationMinutes;
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
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setProblemIds(List<Long> problemIds) {
        this.problemIds = problemIds;
    }

    @Generated
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CreateContestRequest)) {
            return false;
        }
        CreateContestRequest other = (CreateContestRequest)o;
        if (!other.canEqual(this)) {
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
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        List<Long> this$problemIds = this.getProblemIds();
        List<Long> other$problemIds = other.getProblemIds();
        return !(this$problemIds == null ? other$problemIds != null : !((Object)this$problemIds).equals(other$problemIds));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CreateContestRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $durationMinutes = this.getDurationMinutes();
        result = result * 59 + ($durationMinutes == null ? 43 : ((Object)$durationMinutes).hashCode());
        Integer $maxParticipants = this.getMaxParticipants();
        result = result * 59 + ($maxParticipants == null ? 43 : ((Object)$maxParticipants).hashCode());
        Boolean $isPublic = this.getIsPublic();
        result = result * 59 + ($isPublic == null ? 43 : ((Object)$isPublic).hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        List<Long> $problemIds = this.getProblemIds();
        result = result * 59 + ($problemIds == null ? 43 : ((Object)$problemIds).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CreateContestRequest(name=" + this.getName() + ", description=" + this.getDescription() + ", problemIds=" + String.valueOf(this.getProblemIds()) + ", durationMinutes=" + this.getDurationMinutes() + ", maxParticipants=" + this.getMaxParticipants() + ", isPublic=" + this.getIsPublic() + ")";
    }

    @Generated
    public CreateContestRequest() {
    }

    @Generated
    public CreateContestRequest(String name, String description, List<Long> problemIds, Integer durationMinutes, Integer maxParticipants, Boolean isPublic) {
        this.name = name;
        this.description = description;
        this.problemIds = problemIds;
        this.durationMinutes = durationMinutes;
        this.maxParticipants = maxParticipants;
        this.isPublic = isPublic;
    }

    @Generated
    public static class CreateContestRequestBuilder {
        @Generated
        private String name;
        @Generated
        private String description;
        @Generated
        private List<Long> problemIds;
        @Generated
        private Integer durationMinutes;
        @Generated
        private Integer maxParticipants;
        @Generated
        private Boolean isPublic;

        @Generated
        CreateContestRequestBuilder() {
        }

        @Generated
        public CreateContestRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        @Generated
        public CreateContestRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public CreateContestRequestBuilder problemIds(List<Long> problemIds) {
            this.problemIds = problemIds;
            return this;
        }

        @Generated
        public CreateContestRequestBuilder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }

        @Generated
        public CreateContestRequestBuilder maxParticipants(Integer maxParticipants) {
            this.maxParticipants = maxParticipants;
            return this;
        }

        @Generated
        public CreateContestRequestBuilder isPublic(Boolean isPublic) {
            this.isPublic = isPublic;
            return this;
        }

        @Generated
        public CreateContestRequest build() {
            return new CreateContestRequest(this.name, this.description, this.problemIds, this.durationMinutes, this.maxParticipants, this.isPublic);
        }

        @Generated
        public String toString() {
            return "CreateContestRequest.CreateContestRequestBuilder(name=" + this.name + ", description=" + this.description + ", problemIds=" + String.valueOf(this.problemIds) + ", durationMinutes=" + this.durationMinutes + ", maxParticipants=" + this.maxParticipants + ", isPublic=" + this.isPublic + ")";
        }
    }
}
