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

@Document(collection="contest_submissions")
public class ContestSubmission {
    @Id
    private String id;
    private Long contestId;
    private Long userId;
    private String userName;
    private Long problemId;
    private String problemTitle;
    private String code;
    private String language;
    private Boolean isAccepted;
    private Integer score;
    private Long executionTimeMs;
    private Integer memoryUsedKb;
    private String status;
    private String errorMessage;
    private Integer testCasesPassed;
    private Integer totalTestCases;
    private Instant submittedAt;
    private Instant judgedAt;
    private Long timeFromStartMs;

    @Generated
    public static ContestSubmissionBuilder builder() {
        return new ContestSubmissionBuilder();
    }

    @Generated
    public String getId() {
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
    public Long getProblemId() {
        return this.problemId;
    }

    @Generated
    public String getProblemTitle() {
        return this.problemTitle;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public Boolean getIsAccepted() {
        return this.isAccepted;
    }

    @Generated
    public Integer getScore() {
        return this.score;
    }

    @Generated
    public Long getExecutionTimeMs() {
        return this.executionTimeMs;
    }

    @Generated
    public Integer getMemoryUsedKb() {
        return this.memoryUsedKb;
    }

    @Generated
    public String getStatus() {
        return this.status;
    }

    @Generated
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Generated
    public Integer getTestCasesPassed() {
        return this.testCasesPassed;
    }

    @Generated
    public Integer getTotalTestCases() {
        return this.totalTestCases;
    }

    @Generated
    public Instant getSubmittedAt() {
        return this.submittedAt;
    }

    @Generated
    public Instant getJudgedAt() {
        return this.judgedAt;
    }

    @Generated
    public Long getTimeFromStartMs() {
        return this.timeFromStartMs;
    }

    @Generated
    public void setId(String id) {
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
    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    @Generated
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    @Generated
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    @Generated
    public void setScore(Integer score) {
        this.score = score;
    }

    @Generated
    public void setExecutionTimeMs(Long executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    @Generated
    public void setMemoryUsedKb(Integer memoryUsedKb) {
        this.memoryUsedKb = memoryUsedKb;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
    }

    @Generated
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Generated
    public void setTestCasesPassed(Integer testCasesPassed) {
        this.testCasesPassed = testCasesPassed;
    }

    @Generated
    public void setTotalTestCases(Integer totalTestCases) {
        this.totalTestCases = totalTestCases;
    }

    @Generated
    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    @Generated
    public void setJudgedAt(Instant judgedAt) {
        this.judgedAt = judgedAt;
    }

    @Generated
    public void setTimeFromStartMs(Long timeFromStartMs) {
        this.timeFromStartMs = timeFromStartMs;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContestSubmission)) {
            return false;
        }
        ContestSubmission other = (ContestSubmission)o;
        if (!other.canEqual(this)) {
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
        Long this$problemId = this.getProblemId();
        Long other$problemId = other.getProblemId();
        if (this$problemId == null ? other$problemId != null : !((Object)this$problemId).equals(other$problemId)) {
            return false;
        }
        Boolean this$isAccepted = this.getIsAccepted();
        Boolean other$isAccepted = other.getIsAccepted();
        if (this$isAccepted == null ? other$isAccepted != null : !((Object)this$isAccepted).equals(other$isAccepted)) {
            return false;
        }
        Integer this$score = this.getScore();
        Integer other$score = other.getScore();
        if (this$score == null ? other$score != null : !((Object)this$score).equals(other$score)) {
            return false;
        }
        Long this$executionTimeMs = this.getExecutionTimeMs();
        Long other$executionTimeMs = other.getExecutionTimeMs();
        if (this$executionTimeMs == null ? other$executionTimeMs != null : !((Object)this$executionTimeMs).equals(other$executionTimeMs)) {
            return false;
        }
        Integer this$memoryUsedKb = this.getMemoryUsedKb();
        Integer other$memoryUsedKb = other.getMemoryUsedKb();
        if (this$memoryUsedKb == null ? other$memoryUsedKb != null : !((Object)this$memoryUsedKb).equals(other$memoryUsedKb)) {
            return false;
        }
        Integer this$testCasesPassed = this.getTestCasesPassed();
        Integer other$testCasesPassed = other.getTestCasesPassed();
        if (this$testCasesPassed == null ? other$testCasesPassed != null : !((Object)this$testCasesPassed).equals(other$testCasesPassed)) {
            return false;
        }
        Integer this$totalTestCases = this.getTotalTestCases();
        Integer other$totalTestCases = other.getTotalTestCases();
        if (this$totalTestCases == null ? other$totalTestCases != null : !((Object)this$totalTestCases).equals(other$totalTestCases)) {
            return false;
        }
        Long this$timeFromStartMs = this.getTimeFromStartMs();
        Long other$timeFromStartMs = other.getTimeFromStartMs();
        if (this$timeFromStartMs == null ? other$timeFromStartMs != null : !((Object)this$timeFromStartMs).equals(other$timeFromStartMs)) {
            return false;
        }
        String this$id = this.getId();
        String other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) {
            return false;
        }
        String this$userName = this.getUserName();
        String other$userName = other.getUserName();
        if (this$userName == null ? other$userName != null : !this$userName.equals(other$userName)) {
            return false;
        }
        String this$problemTitle = this.getProblemTitle();
        String other$problemTitle = other.getProblemTitle();
        if (this$problemTitle == null ? other$problemTitle != null : !this$problemTitle.equals(other$problemTitle)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$language = this.getLanguage();
        String other$language = other.getLanguage();
        if (this$language == null ? other$language != null : !this$language.equals(other$language)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        String this$errorMessage = this.getErrorMessage();
        String other$errorMessage = other.getErrorMessage();
        if (this$errorMessage == null ? other$errorMessage != null : !this$errorMessage.equals(other$errorMessage)) {
            return false;
        }
        Instant this$submittedAt = this.getSubmittedAt();
        Instant other$submittedAt = other.getSubmittedAt();
        if (this$submittedAt == null ? other$submittedAt != null : !((Object)this$submittedAt).equals(other$submittedAt)) {
            return false;
        }
        Instant this$judgedAt = this.getJudgedAt();
        Instant other$judgedAt = other.getJudgedAt();
        return !(this$judgedAt == null ? other$judgedAt != null : !((Object)this$judgedAt).equals(other$judgedAt));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ContestSubmission;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $contestId = this.getContestId();
        result = result * 59 + ($contestId == null ? 43 : ((Object)$contestId).hashCode());
        Long $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
        Long $problemId = this.getProblemId();
        result = result * 59 + ($problemId == null ? 43 : ((Object)$problemId).hashCode());
        Boolean $isAccepted = this.getIsAccepted();
        result = result * 59 + ($isAccepted == null ? 43 : ((Object)$isAccepted).hashCode());
        Integer $score = this.getScore();
        result = result * 59 + ($score == null ? 43 : ((Object)$score).hashCode());
        Long $executionTimeMs = this.getExecutionTimeMs();
        result = result * 59 + ($executionTimeMs == null ? 43 : ((Object)$executionTimeMs).hashCode());
        Integer $memoryUsedKb = this.getMemoryUsedKb();
        result = result * 59 + ($memoryUsedKb == null ? 43 : ((Object)$memoryUsedKb).hashCode());
        Integer $testCasesPassed = this.getTestCasesPassed();
        result = result * 59 + ($testCasesPassed == null ? 43 : ((Object)$testCasesPassed).hashCode());
        Integer $totalTestCases = this.getTotalTestCases();
        result = result * 59 + ($totalTestCases == null ? 43 : ((Object)$totalTestCases).hashCode());
        Long $timeFromStartMs = this.getTimeFromStartMs();
        result = result * 59 + ($timeFromStartMs == null ? 43 : ((Object)$timeFromStartMs).hashCode());
        String $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        String $userName = this.getUserName();
        result = result * 59 + ($userName == null ? 43 : $userName.hashCode());
        String $problemTitle = this.getProblemTitle();
        result = result * 59 + ($problemTitle == null ? 43 : $problemTitle.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        String $errorMessage = this.getErrorMessage();
        result = result * 59 + ($errorMessage == null ? 43 : $errorMessage.hashCode());
        Instant $submittedAt = this.getSubmittedAt();
        result = result * 59 + ($submittedAt == null ? 43 : ((Object)$submittedAt).hashCode());
        Instant $judgedAt = this.getJudgedAt();
        result = result * 59 + ($judgedAt == null ? 43 : ((Object)$judgedAt).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ContestSubmission(id=" + this.getId() + ", contestId=" + this.getContestId() + ", userId=" + this.getUserId() + ", userName=" + this.getUserName() + ", problemId=" + this.getProblemId() + ", problemTitle=" + this.getProblemTitle() + ", code=" + this.getCode() + ", language=" + this.getLanguage() + ", isAccepted=" + this.getIsAccepted() + ", score=" + this.getScore() + ", executionTimeMs=" + this.getExecutionTimeMs() + ", memoryUsedKb=" + this.getMemoryUsedKb() + ", status=" + this.getStatus() + ", errorMessage=" + this.getErrorMessage() + ", testCasesPassed=" + this.getTestCasesPassed() + ", totalTestCases=" + this.getTotalTestCases() + ", submittedAt=" + String.valueOf(this.getSubmittedAt()) + ", judgedAt=" + String.valueOf(this.getJudgedAt()) + ", timeFromStartMs=" + this.getTimeFromStartMs() + ")";
    }

    @Generated
    public ContestSubmission() {
    }

    @Generated
    public ContestSubmission(String id, Long contestId, Long userId, String userName, Long problemId, String problemTitle, String code, String language, Boolean isAccepted, Integer score, Long executionTimeMs, Integer memoryUsedKb, String status, String errorMessage, Integer testCasesPassed, Integer totalTestCases, Instant submittedAt, Instant judgedAt, Long timeFromStartMs) {
        this.id = id;
        this.contestId = contestId;
        this.userId = userId;
        this.userName = userName;
        this.problemId = problemId;
        this.problemTitle = problemTitle;
        this.code = code;
        this.language = language;
        this.isAccepted = isAccepted;
        this.score = score;
        this.executionTimeMs = executionTimeMs;
        this.memoryUsedKb = memoryUsedKb;
        this.status = status;
        this.errorMessage = errorMessage;
        this.testCasesPassed = testCasesPassed;
        this.totalTestCases = totalTestCases;
        this.submittedAt = submittedAt;
        this.judgedAt = judgedAt;
        this.timeFromStartMs = timeFromStartMs;
    }

    @Generated
    public static class ContestSubmissionBuilder {
        @Generated
        private String id;
        @Generated
        private Long contestId;
        @Generated
        private Long userId;
        @Generated
        private String userName;
        @Generated
        private Long problemId;
        @Generated
        private String problemTitle;
        @Generated
        private String code;
        @Generated
        private String language;
        @Generated
        private Boolean isAccepted;
        @Generated
        private Integer score;
        @Generated
        private Long executionTimeMs;
        @Generated
        private Integer memoryUsedKb;
        @Generated
        private String status;
        @Generated
        private String errorMessage;
        @Generated
        private Integer testCasesPassed;
        @Generated
        private Integer totalTestCases;
        @Generated
        private Instant submittedAt;
        @Generated
        private Instant judgedAt;
        @Generated
        private Long timeFromStartMs;

        @Generated
        ContestSubmissionBuilder() {
        }

        @Generated
        public ContestSubmissionBuilder id(String id) {
            this.id = id;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder contestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder problemId(Long problemId) {
            this.problemId = problemId;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder problemTitle(String problemTitle) {
            this.problemTitle = problemTitle;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder code(String code) {
            this.code = code;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder isAccepted(Boolean isAccepted) {
            this.isAccepted = isAccepted;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder score(Integer score) {
            this.score = score;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder executionTimeMs(Long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder memoryUsedKb(Integer memoryUsedKb) {
            this.memoryUsedKb = memoryUsedKb;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder status(String status) {
            this.status = status;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder testCasesPassed(Integer testCasesPassed) {
            this.testCasesPassed = testCasesPassed;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder totalTestCases(Integer totalTestCases) {
            this.totalTestCases = totalTestCases;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder submittedAt(Instant submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder judgedAt(Instant judgedAt) {
            this.judgedAt = judgedAt;
            return this;
        }

        @Generated
        public ContestSubmissionBuilder timeFromStartMs(Long timeFromStartMs) {
            this.timeFromStartMs = timeFromStartMs;
            return this;
        }

        @Generated
        public ContestSubmission build() {
            return new ContestSubmission(this.id, this.contestId, this.userId, this.userName, this.problemId, this.problemTitle, this.code, this.language, this.isAccepted, this.score, this.executionTimeMs, this.memoryUsedKb, this.status, this.errorMessage, this.testCasesPassed, this.totalTestCases, this.submittedAt, this.judgedAt, this.timeFromStartMs);
        }

        @Generated
        public String toString() {
            return "ContestSubmission.ContestSubmissionBuilder(id=" + this.id + ", contestId=" + this.contestId + ", userId=" + this.userId + ", userName=" + this.userName + ", problemId=" + this.problemId + ", problemTitle=" + this.problemTitle + ", code=" + this.code + ", language=" + this.language + ", isAccepted=" + this.isAccepted + ", score=" + this.score + ", executionTimeMs=" + this.executionTimeMs + ", memoryUsedKb=" + this.memoryUsedKb + ", status=" + this.status + ", errorMessage=" + this.errorMessage + ", testCasesPassed=" + this.testCasesPassed + ", totalTestCases=" + this.totalTestCases + ", submittedAt=" + String.valueOf(this.submittedAt) + ", judgedAt=" + String.valueOf(this.judgedAt) + ", timeFromStartMs=" + this.timeFromStartMs + ")";
        }
    }
}
