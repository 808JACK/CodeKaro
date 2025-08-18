/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.time.Instant;
import lombok.Generated;

public class ContestSubmissionResponse {
    private String submissionId;
    private String contestCode;
    private Long problemId;
    private String status;
    private Boolean isAccepted;
    private Integer score;
    private Long executionTimeMs;
    private Integer memoryUsedKb;
    private String errorMessage;
    private Integer testCasesPassed;
    private Integer totalTestCases;
    private Instant submittedAt;
    private String message;

    @Generated
    public static ContestSubmissionResponseBuilder builder() {
        return new ContestSubmissionResponseBuilder();
    }

    @Generated
    public String getSubmissionId() {
        return this.submissionId;
    }

    @Generated
    public String getContestCode() {
        return this.contestCode;
    }

    @Generated
    public Long getProblemId() {
        return this.problemId;
    }

    @Generated
    public String getStatus() {
        return this.status;
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
    public String getMessage() {
        return this.message;
    }

    @Generated
    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    @Generated
    public void setContestCode(String contestCode) {
        this.contestCode = contestCode;
    }

    @Generated
    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    @Generated
    public void setStatus(String status) {
        this.status = status;
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
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ContestSubmissionResponse)) {
            return false;
        }
        ContestSubmissionResponse other = (ContestSubmissionResponse)o;
        if (!other.canEqual(this)) {
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
        String this$submissionId = this.getSubmissionId();
        String other$submissionId = other.getSubmissionId();
        if (this$submissionId == null ? other$submissionId != null : !this$submissionId.equals(other$submissionId)) {
            return false;
        }
        String this$contestCode = this.getContestCode();
        String other$contestCode = other.getContestCode();
        if (this$contestCode == null ? other$contestCode != null : !this$contestCode.equals(other$contestCode)) {
            return false;
        }
        String this$status = this.getStatus();
        String other$status = other.getStatus();
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) {
            return false;
        }
        Instant this$submittedAt = this.getSubmittedAt();
        Instant other$submittedAt = other.getSubmittedAt();
        if (this$submittedAt == null ? other$submittedAt != null : !((Object)this$submittedAt).equals(other$submittedAt)) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        return !(this$message == null ? other$message != null : !this$message.equals(other$message));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ContestSubmissionResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $problemId = this.getProblemId();
        result = result * 59 + ($problemId == null ? 43 : ((Object)$problemId).hashCode());
        Boolean $isAccepted = this.getIsAccepted();
        result = result * 59 + ($isAccepted == null ? 43 : ((Object)$isAccepted).hashCode());
        Integer $testCasesPassed = this.getTestCasesPassed();
        result = result * 59 + ($testCasesPassed == null ? 43 : ((Object)$testCasesPassed).hashCode());
        Integer $totalTestCases = this.getTotalTestCases();
        result = result * 59 + ($totalTestCases == null ? 43 : ((Object)$totalTestCases).hashCode());
        String $submissionId = this.getSubmissionId();
        result = result * 59 + ($submissionId == null ? 43 : $submissionId.hashCode());
        String $contestCode = this.getContestCode();
        result = result * 59 + ($contestCode == null ? 43 : $contestCode.hashCode());
        String $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Instant $submittedAt = this.getSubmittedAt();
        result = result * 59 + ($submittedAt == null ? 43 : ((Object)$submittedAt).hashCode());
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ContestSubmissionResponse(submissionId=" + this.getSubmissionId() + ", contestCode=" + this.getContestCode() + ", problemId=" + this.getProblemId() + ", status=" + this.getStatus() + ", isAccepted=" + this.getIsAccepted() + ", testCasesPassed=" + this.getTestCasesPassed() + ", totalTestCases=" + this.getTotalTestCases() + ", submittedAt=" + String.valueOf(this.getSubmittedAt()) + ", message=" + this.getMessage() + ")";
    }

    @Generated
    public ContestSubmissionResponse() {
    }

    @Generated
    public ContestSubmissionResponse(String submissionId, String contestCode, Long problemId, String status, Boolean isAccepted, Integer testCasesPassed, Integer totalTestCases, Instant submittedAt, String message) {
        this.submissionId = submissionId;
        this.contestCode = contestCode;
        this.problemId = problemId;
        this.status = status;
        this.isAccepted = isAccepted;
        this.testCasesPassed = testCasesPassed;
        this.totalTestCases = totalTestCases;
        this.submittedAt = submittedAt;
        this.message = message;
    }

    @Generated
    public static class ContestSubmissionResponseBuilder {
        @Generated
        private String submissionId;
        @Generated
        private String contestCode;
        @Generated
        private Long problemId;
        @Generated
        private String status;
        @Generated
        private Boolean isAccepted;
        @Generated
        private Integer testCasesPassed;
        @Generated
        private Integer totalTestCases;
        @Generated
        private Instant submittedAt;
        @Generated
        private String message;

        @Generated
        ContestSubmissionResponseBuilder() {
        }

        @Generated
        public ContestSubmissionResponseBuilder submissionId(String submissionId) {
            this.submissionId = submissionId;
            return this;
        }

        @Generated
        public ContestSubmissionResponseBuilder contestCode(String contestCode) {
            this.contestCode = contestCode;
            return this;
        }

        @Generated
        public ContestSubmissionResponseBuilder problemId(Long problemId) {
            this.problemId = problemId;
            return this;
        }

        @Generated
        public ContestSubmissionResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        @Generated
        public ContestSubmissionResponseBuilder isAccepted(Boolean isAccepted) {
            this.isAccepted = isAccepted;
            return this;
        }

        @Generated
        public ContestSubmissionResponseBuilder testCasesPassed(Integer testCasesPassed) {
            this.testCasesPassed = testCasesPassed;
            return this;
        }

        @Generated
        public ContestSubmissionResponseBuilder totalTestCases(Integer totalTestCases) {
            this.totalTestCases = totalTestCases;
            return this;
        }

        @Generated
        public ContestSubmissionResponseBuilder submittedAt(Instant submittedAt) {
            this.submittedAt = submittedAt;
            return this;
        }

        @Generated
        public ContestSubmissionResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        @Generated
        public ContestSubmissionResponse build() {
            return new ContestSubmissionResponse(this.submissionId, this.contestCode, this.problemId, this.status, this.isAccepted, this.testCasesPassed, this.totalTestCases, this.submittedAt, this.message);
        }

        @Generated
        public String toString() {
            return "ContestSubmissionResponse.ContestSubmissionResponseBuilder(submissionId=" + this.submissionId + ", contestCode=" + this.contestCode + ", problemId=" + this.problemId + ", status=" + this.status + ", isAccepted=" + this.isAccepted + ", testCasesPassed=" + this.testCasesPassed + ", totalTestCases=" + this.totalTestCases + ", submittedAt=" + String.valueOf(this.submittedAt) + ", message=" + this.message + ")";
        }
    }
}
