/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Generated;

public class ContestDetailsResponse {
    private String contestCode;
    private String name;
    private List<Long> problemIds;
    private Long currentProblemId;
    private ZonedDateTime startTime;
    private Integer durationMinutes;
    private Long remainingTimeSeconds;
    private Boolean isCreator;

    @Generated
    public static ContestDetailsResponseBuilder builder() {
        return new ContestDetailsResponseBuilder();
    }

    @Generated
    public String getContestCode() {
        return this.contestCode;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public List<Long> getProblemIds() {
        return this.problemIds;
    }

    @Generated
    public Long getCurrentProblemId() {
        return this.currentProblemId;
    }

    @Generated
    public ZonedDateTime getStartTime() {
        return this.startTime;
    }

    @Generated
    public Integer getDurationMinutes() {
        return this.durationMinutes;
    }

    @Generated
    public Long getRemainingTimeSeconds() {
        return this.remainingTimeSeconds;
    }

    @Generated
    public Boolean getIsCreator() {
        return this.isCreator;
    }

    @Generated
    public void setContestCode(String contestCode) {
        this.contestCode = contestCode;
    }

    @Generated
    public void setName(String name) {
        this.name = name;
    }

    @Generated
    public void setProblemIds(List<Long> problemIds) {
        this.problemIds = problemIds;
    }

    @Generated
    public void setCurrentProblemId(Long currentProblemId) {
        this.currentProblemId = currentProblemId;
    }

    @Generated
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    @Generated
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    @Generated
    public void setRemainingTimeSeconds(Long remainingTimeSeconds) {
        this.remainingTimeSeconds = remainingTimeSeconds;
    }

    @Generated
    public void setIsCreator(Boolean isCreator) {
        this.isCreator = isCreator;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContestDetailsResponse)) {
            return false;
        }
        ContestDetailsResponse other = (ContestDetailsResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$currentProblemId = this.getCurrentProblemId();
        Long other$currentProblemId = other.getCurrentProblemId();
        if (this$currentProblemId == null ? other$currentProblemId != null : !((Object)this$currentProblemId).equals(other$currentProblemId)) {
            return false;
        }
        Integer this$durationMinutes = this.getDurationMinutes();
        Integer other$durationMinutes = other.getDurationMinutes();
        if (this$durationMinutes == null ? other$durationMinutes != null : !((Object)this$durationMinutes).equals(other$durationMinutes)) {
            return false;
        }
        Long this$remainingTimeSeconds = this.getRemainingTimeSeconds();
        Long other$remainingTimeSeconds = other.getRemainingTimeSeconds();
        if (this$remainingTimeSeconds == null ? other$remainingTimeSeconds != null : !((Object)this$remainingTimeSeconds).equals(other$remainingTimeSeconds)) {
            return false;
        }
        Boolean this$isCreator = this.getIsCreator();
        Boolean other$isCreator = other.getIsCreator();
        if (this$isCreator == null ? other$isCreator != null : !((Object)this$isCreator).equals(other$isCreator)) {
            return false;
        }
        String this$contestCode = this.getContestCode();
        String other$contestCode = other.getContestCode();
        if (this$contestCode == null ? other$contestCode != null : !this$contestCode.equals(other$contestCode)) {
            return false;
        }
        String this$name = this.getName();
        String other$name = other.getName();
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) {
            return false;
        }
        List<Long> this$problemIds = this.getProblemIds();
        List<Long> other$problemIds = other.getProblemIds();
        if (this$problemIds == null ? other$problemIds != null : !((Object)this$problemIds).equals(other$problemIds)) {
            return false;
        }
        ZonedDateTime this$startTime = this.getStartTime();
        ZonedDateTime other$startTime = other.getStartTime();
        return !(this$startTime == null ? other$startTime != null : !((Object)this$startTime).equals(other$startTime));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ContestDetailsResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $currentProblemId = this.getCurrentProblemId();
        result = result * 59 + ($currentProblemId == null ? 43 : ((Object)$currentProblemId).hashCode());
        Integer $durationMinutes = this.getDurationMinutes();
        result = result * 59 + ($durationMinutes == null ? 43 : ((Object)$durationMinutes).hashCode());
        Long $remainingTimeSeconds = this.getRemainingTimeSeconds();
        result = result * 59 + ($remainingTimeSeconds == null ? 43 : ((Object)$remainingTimeSeconds).hashCode());
        Boolean $isCreator = this.getIsCreator();
        result = result * 59 + ($isCreator == null ? 43 : ((Object)$isCreator).hashCode());
        String $contestCode = this.getContestCode();
        result = result * 59 + ($contestCode == null ? 43 : $contestCode.hashCode());
        String $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        List<Long> $problemIds = this.getProblemIds();
        result = result * 59 + ($problemIds == null ? 43 : ((Object)$problemIds).hashCode());
        ZonedDateTime $startTime = this.getStartTime();
        result = result * 59 + ($startTime == null ? 43 : ((Object)$startTime).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ContestDetailsResponse(contestCode=" + this.getContestCode() + ", name=" + this.getName() + ", problemIds=" + String.valueOf(this.getProblemIds()) + ", currentProblemId=" + this.getCurrentProblemId() + ", startTime=" + String.valueOf(this.getStartTime()) + ", durationMinutes=" + this.getDurationMinutes() + ", remainingTimeSeconds=" + this.getRemainingTimeSeconds() + ", isCreator=" + this.getIsCreator() + ")";
    }

    @Generated
    public ContestDetailsResponse() {
    }

    @Generated
    public ContestDetailsResponse(String contestCode, String name, List<Long> problemIds, Long currentProblemId, ZonedDateTime startTime, Integer durationMinutes, Long remainingTimeSeconds, Boolean isCreator) {
        this.contestCode = contestCode;
        this.name = name;
        this.problemIds = problemIds;
        this.currentProblemId = currentProblemId;
        this.startTime = startTime;
        this.durationMinutes = durationMinutes;
        this.remainingTimeSeconds = remainingTimeSeconds;
        this.isCreator = isCreator;
    }

    @Generated
    public static class ContestDetailsResponseBuilder {
        @Generated
        private String contestCode;
        @Generated
        private String name;
        @Generated
        private List<Long> problemIds;
        @Generated
        private Long currentProblemId;
        @Generated
        private ZonedDateTime startTime;
        @Generated
        private Integer durationMinutes;
        @Generated
        private Long remainingTimeSeconds;
        @Generated
        private Boolean isCreator;

        @Generated
        ContestDetailsResponseBuilder() {
        }

        @Generated
        public ContestDetailsResponseBuilder contestCode(String contestCode) {
            this.contestCode = contestCode;
            return this;
        }

        @Generated
        public ContestDetailsResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        @Generated
        public ContestDetailsResponseBuilder problemIds(List<Long> problemIds) {
            this.problemIds = problemIds;
            return this;
        }

        @Generated
        public ContestDetailsResponseBuilder currentProblemId(Long currentProblemId) {
            this.currentProblemId = currentProblemId;
            return this;
        }

        @Generated
        public ContestDetailsResponseBuilder startTime(ZonedDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        @Generated
        public ContestDetailsResponseBuilder durationMinutes(Integer durationMinutes) {
            this.durationMinutes = durationMinutes;
            return this;
        }

        @Generated
        public ContestDetailsResponseBuilder remainingTimeSeconds(Long remainingTimeSeconds) {
            this.remainingTimeSeconds = remainingTimeSeconds;
            return this;
        }

        @Generated
        public ContestDetailsResponseBuilder isCreator(Boolean isCreator) {
            this.isCreator = isCreator;
            return this;
        }

        @Generated
        public ContestDetailsResponse build() {
            return new ContestDetailsResponse(this.contestCode, this.name, this.problemIds, this.currentProblemId, this.startTime, this.durationMinutes, this.remainingTimeSeconds, this.isCreator);
        }

        @Generated
        public String toString() {
            return "ContestDetailsResponse.ContestDetailsResponseBuilder(contestCode=" + this.contestCode + ", name=" + this.name + ", problemIds=" + String.valueOf(this.problemIds) + ", currentProblemId=" + this.currentProblemId + ", startTime=" + String.valueOf(this.startTime) + ", durationMinutes=" + this.durationMinutes + ", remainingTimeSeconds=" + this.remainingTimeSeconds + ", isCreator=" + this.isCreator + ")";
        }
    }
}
