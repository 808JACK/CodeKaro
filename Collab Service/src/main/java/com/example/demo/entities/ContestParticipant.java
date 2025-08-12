/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jakarta.persistence.Column
 *  jakarta.persistence.Entity
 *  jakarta.persistence.GeneratedValue
 *  jakarta.persistence.GenerationType
 *  jakarta.persistence.Id
 *  jakarta.persistence.PrePersist
 *  jakarta.persistence.Table
 *  jakarta.persistence.UniqueConstraint
 *  lombok.Generated
 */
package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.ZonedDateTime;
import lombok.Generated;

@Entity
@Table(name="contest_participants", uniqueConstraints={@UniqueConstraint(columnNames={"contest_id", "user_id"})})
public class ContestParticipant {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="contest_id", nullable=false)
    private Long contestId;
    @Column(name="user_id", nullable=false)
    private Long userId;
    @Column(name="user_name")
    private String userName;
    @Column(name="user_email")
    private String userEmail;
    @Column(name="total_score", nullable=false)
    private Integer totalScore;
    @Column(name="total_time_ms", nullable=false)
    private Long totalTimeMs;
    @Column(name="problems_solved", nullable=false)
    private Integer problemsSolved;
    @Column(name="rank_position")
    private Integer rankPosition;
    @Column(name="joined_at", nullable=false)
    private ZonedDateTime joinedAt;
    @Column(name="last_submission_at")
    private ZonedDateTime lastSubmissionAt;
    @Column(name="is_active", nullable=false)
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        this.joinedAt = ZonedDateTime.now();
        if (this.totalScore == null) {
            this.totalScore = 0;
        }
        if (this.totalTimeMs == null) {
            this.totalTimeMs = 0L;
        }
        if (this.problemsSolved == null) {
            this.problemsSolved = 0;
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @Generated
    public static ContestParticipantBuilder builder() {
        return new ContestParticipantBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public Long getContestId() {
        return this.contestId;
    }

    @Generated
    public Long getUserId() {
        return this.userId;
    }

    @Generated
    public String getUserName() {
        return this.userName;
    }

    @Generated
    public String getUserEmail() {
        return this.userEmail;
    }

    @Generated
    public Integer getTotalScore() {
        return this.totalScore;
    }

    @Generated
    public Long getTotalTimeMs() {
        return this.totalTimeMs;
    }

    @Generated
    public Integer getProblemsSolved() {
        return this.problemsSolved;
    }

    @Generated
    public Integer getRankPosition() {
        return this.rankPosition;
    }

    @Generated
    public ZonedDateTime getJoinedAt() {
        return this.joinedAt;
    }

    @Generated
    public ZonedDateTime getLastSubmissionAt() {
        return this.lastSubmissionAt;
    }

    @Generated
    public Boolean getIsActive() {
        return this.isActive;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setContestId(Long contestId) {
        this.contestId = contestId;
    }

    @Generated
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Generated
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Generated
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Generated
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    @Generated
    public void setTotalTimeMs(Long totalTimeMs) {
        this.totalTimeMs = totalTimeMs;
    }

    @Generated
    public void setProblemsSolved(Integer problemsSolved) {
        this.problemsSolved = problemsSolved;
    }

    @Generated
    public void setRankPosition(Integer rankPosition) {
        this.rankPosition = rankPosition;
    }

    @Generated
    public void setJoinedAt(ZonedDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Generated
    public void setLastSubmissionAt(ZonedDateTime lastSubmissionAt) {
        this.lastSubmissionAt = lastSubmissionAt;
    }

    @Generated
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContestParticipant)) {
            return false;
        }
        ContestParticipant other = (ContestParticipant)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Long this$contestId = this.getContestId();
        Long other$contestId = other.getContestId();
        if (this$contestId == null ? other$contestId != null : !((Object)this$contestId).equals(other$contestId)) {
            return false;
        }
        Long this$userId = this.getUserId();
        Long other$userId = other.getUserId();
        if (this$userId == null ? other$userId != null : !((Object)this$userId).equals(other$userId)) {
            return false;
        }
        Integer this$totalScore = this.getTotalScore();
        Integer other$totalScore = other.getTotalScore();
        if (this$totalScore == null ? other$totalScore != null : !((Object)this$totalScore).equals(other$totalScore)) {
            return false;
        }
        Long this$totalTimeMs = this.getTotalTimeMs();
        Long other$totalTimeMs = other.getTotalTimeMs();
        if (this$totalTimeMs == null ? other$totalTimeMs != null : !((Object)this$totalTimeMs).equals(other$totalTimeMs)) {
            return false;
        }
        Integer this$problemsSolved = this.getProblemsSolved();
        Integer other$problemsSolved = other.getProblemsSolved();
        if (this$problemsSolved == null ? other$problemsSolved != null : !((Object)this$problemsSolved).equals(other$problemsSolved)) {
            return false;
        }
        Integer this$rankPosition = this.getRankPosition();
        Integer other$rankPosition = other.getRankPosition();
        if (this$rankPosition == null ? other$rankPosition != null : !((Object)this$rankPosition).equals(other$rankPosition)) {
            return false;
        }
        Boolean this$isActive = this.getIsActive();
        Boolean other$isActive = other.getIsActive();
        if (this$isActive == null ? other$isActive != null : !((Object)this$isActive).equals(other$isActive)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$userEmail = this.getUserEmail();
        String other$userEmail = other.getUserEmail();
        if (this$userEmail == null ? other$userEmail != null : !this$userEmail.equals(other$userEmail)) {
            return false;
        }
        ZonedDateTime this$joinedAt = this.getJoinedAt();
        ZonedDateTime other$joinedAt = other.getJoinedAt();
        if (this$joinedAt == null ? other$joinedAt != null : !((Object)this$joinedAt).equals(other$joinedAt)) {
            return false;
        }
        ZonedDateTime this$lastSubmissionAt = this.getLastSubmissionAt();
        ZonedDateTime other$lastSubmissionAt = other.getLastSubmissionAt();
        return !(this$lastSubmissionAt == null ? other$lastSubmissionAt != null : !((Object)this$lastSubmissionAt).equals(other$lastSubmissionAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ContestParticipant;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Long $contestId = this.getContestId();
        result = result * 59 + ($contestId == null ? 43 : ((Object)$contestId).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Integer $totalScore = this.getTotalScore();
        result = result * 59 + ($totalScore == null ? 43 : ((Object)$totalScore).hashCode());
        Long $totalTimeMs = this.getTotalTimeMs();
        result = result * 59 + ($totalTimeMs == null ? 43 : ((Object)$totalTimeMs).hashCode());
        Integer $problemsSolved = this.getProblemsSolved();
        result = result * 59 + ($problemsSolved == null ? 43 : ((Object)$problemsSolved).hashCode());
        Integer $rankPosition = this.getRankPosition();
        result = result * 59 + ($rankPosition == null ? 43 : ((Object)$rankPosition).hashCode());
        Boolean $isActive = this.getIsActive();
        result = result * 59 + ($isActive == null ? 43 : ((Object)$isActive).hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $userEmail = this.getUserEmail();
        result = result * 59 + ($userEmail == null ? 43 : $userEmail.hashCode());
        ZonedDateTime $joinedAt = this.getJoinedAt();
        result = result * 59 + ($joinedAt == null ? 43 : ((Object)$joinedAt).hashCode());
        ZonedDateTime $lastSubmissionAt = this.getLastSubmissionAt();
        result = result * 59 + ($lastSubmissionAt == null ? 43 : ((Object)$lastSubmissionAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ContestParticipant(id=" + this.getId() + ", contestId=" + this.getContestId() + ", userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", userEmail=" + this.getUserEmail() + ", totalScore=" + this.getTotalScore() + ", totalTimeMs=" + this.getTotalTimeMs() + ", problemsSolved=" + this.getProblemsSolved() + ", rankPosition=" + this.getRankPosition() + ", joinedAt=" + String.valueOf(this.getJoinedAt()) + ", lastSubmissionAt=" + String.valueOf(this.getLastSubmissionAt()) + ", isActive=" + this.getIsActive() + ")";
    }

    @Generated
    public ContestParticipant() {
    }

    @Generated
    public ContestParticipant(Long id, Long contestId, Long userId, String userName, String userEmail, Integer totalScore, Long totalTimeMs, Integer problemsSolved, Integer rankPosition, ZonedDateTime joinedAt, ZonedDateTime lastSubmissionAt, Boolean isActive) {
        this.id = id;
        this.contestId = contestId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.totalScore = totalScore;
        this.totalTimeMs = totalTimeMs;
        this.problemsSolved = problemsSolved;
        this.rankPosition = rankPosition;
        this.joinedAt = joinedAt;
        this.lastSubmissionAt = lastSubmissionAt;
        this.isActive = isActive;
    }

    @Generated
    public static class ContestParticipantBuilder {
        @Generated
        private Long id;
        @Generated
        private Long contestId;
        @Generated
        private Long userId;
        @Generated
        private String userName;
        @Generated
        private String userEmail;
        @Generated
        private Integer totalScore;
        @Generated
        private Long totalTimeMs;
        @Generated
        private Integer problemsSolved;
        @Generated
        private Integer rankPosition;
        @Generated
        private ZonedDateTime joinedAt;
        @Generated
        private ZonedDateTime lastSubmissionAt;
        @Generated
        private Boolean isActive;

        @Generated
        ContestParticipantBuilder() {
        }

        @Generated
        public ContestParticipantBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ContestParticipantBuilder contestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        @Generated
        public ContestParticipantBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public ContestParticipantBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        @Generated
        public ContestParticipantBuilder userEmail(String userEmail) {
            this.userEmail = userEmail;
            return this;
        }

        @Generated
        public ContestParticipantBuilder totalScore(Integer totalScore) {
            this.totalScore = totalScore;
            return this;
        }

        @Generated
        public ContestParticipantBuilder totalTimeMs(Long totalTimeMs) {
            this.totalTimeMs = totalTimeMs;
            return this;
        }

        @Generated
        public ContestParticipantBuilder problemsSolved(Integer problemsSolved) {
            this.problemsSolved = problemsSolved;
            return this;
        }

        @Generated
        public ContestParticipantBuilder rankPosition(Integer rankPosition) {
            this.rankPosition = rankPosition;
            return this;
        }

        @Generated
        public ContestParticipantBuilder joinedAt(ZonedDateTime joinedAt) {
            this.joinedAt = joinedAt;
            return this;
        }

        @Generated
        public ContestParticipantBuilder lastSubmissionAt(ZonedDateTime lastSubmissionAt) {
            this.lastSubmissionAt = lastSubmissionAt;
            return this;
        }

        @Generated
        public ContestParticipantBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        @Generated
        public ContestParticipant build() {
            return new ContestParticipant(this.id, this.contestId, this.userId, this.userName, this.userEmail, this.totalScore, this.totalTimeMs, this.problemsSolved, this.rankPosition, this.joinedAt, this.lastSubmissionAt, this.isActive);
        }

        @Generated
        public String toString() {
            return "ContestParticipant.ContestParticipantBuilder(id=" + this.id + ", contestId=" + this.contestId + ", userId=" + this.userId + ", userName=" + this.userName + ", userEmail=" + this.userEmail + ", totalScore=" + this.totalScore + ", totalTimeMs=" + this.totalTimeMs + ", problemsSolved=" + this.problemsSolved + ", rankPosition=" + this.rankPosition + ", joinedAt=" + String.valueOf(this.joinedAt) + ", lastSubmissionAt=" + String.valueOf(this.lastSubmissionAt) + ", isActive=" + this.isActive + ")";
        }
    }
}
