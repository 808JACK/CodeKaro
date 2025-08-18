/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class ContestSubmissionRequest {
    private String contestCode;
    private Long contestId;
    private Long userId;
    private String userName;
    private Long problemId;
    private String problemTitle;
    private String code;
    private String language;
    private Long timeFromStartMs;

    @Generated
    public static ContestSubmissionRequestBuilder builder() {
        return new ContestSubmissionRequestBuilder();
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
    public String getCode() {
        return this.code;
    }

    @Generated
    public String getLanguage() {
        return this.language;
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
    public String getProblemTitle() {
        return this.problemTitle;
    }

    @Generated
    public Long getTimeFromStartMs() {
        return this.timeFromStartMs;
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
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
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
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
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
        if (!(o instanceof ContestSubmissionRequest)) {
            return false;
        }
        ContestSubmissionRequest other = (ContestSubmissionRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$problemId = this.getProblemId();
        Long other$problemId = other.getProblemId();
        if (this$problemId == null ? other$problemId != null : !((Object)this$problemId).equals(other$problemId)) {
            return false;
        }
        String this$contestCode = this.getContestCode();
        String other$contestCode = other.getContestCode();
        if (this$contestCode == null ? other$contestCode != null : !this$contestCode.equals(other$contestCode)) {
            return false;
        }
        String this$code = this.getCode();
        String other$code = other.getCode();
        if (this$code == null ? other$code != null : !this$code.equals(other$code)) {
            return false;
        }
        String this$language = this.getLanguage();
        String other$language = other.getLanguage();
        return !(this$language == null ? other$language != null : !this$language.equals(other$language));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ContestSubmissionRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $problemId = this.getProblemId();
        result = result * 59 + ($problemId == null ? 43 : ((Object)$problemId).hashCode());
        String $contestCode = this.getContestCode();
        result = result * 59 + ($contestCode == null ? 43 : $contestCode.hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ContestSubmissionRequest(contestCode=" + this.getContestCode() + ", problemId=" + this.getProblemId() + ", code=" + this.getCode() + ", language=" + this.getLanguage() + ")";
    }

    @Generated
    public ContestSubmissionRequest() {
    }

    @Generated
    public ContestSubmissionRequest(String contestCode, Long contestId, Long userId, String userName, Long problemId, String problemTitle, String code, String language, Long timeFromStartMs) {
        this.contestCode = contestCode;
        this.contestId = contestId;
        this.userId = userId;
        this.userName = userName;
        this.problemId = problemId;
        this.problemTitle = problemTitle;
        this.code = code;
        this.language = language;
        this.timeFromStartMs = timeFromStartMs;
    }

    @Generated
    public static class ContestSubmissionRequestBuilder {
        @Generated
        private String contestCode;
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
        private Long timeFromStartMs;

        @Generated
        ContestSubmissionRequestBuilder() {
        }

        @Generated
        public ContestSubmissionRequestBuilder contestCode(String contestCode) {
            this.contestCode = contestCode;
            return this;
        }

        @Generated
        public ContestSubmissionRequestBuilder problemId(Long problemId) {
            this.problemId = problemId;
            return this;
        }

        @Generated
        public ContestSubmissionRequestBuilder code(String code) {
            this.code = code;
            return this;
        }

        @Generated
        public ContestSubmissionRequestBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public ContestSubmissionRequestBuilder contestId(Long contestId) {
            this.contestId = contestId;
            return this;
        }

        @Generated
        public ContestSubmissionRequestBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        @Generated
        public ContestSubmissionRequestBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        @Generated
        public ContestSubmissionRequestBuilder problemTitle(String problemTitle) {
            this.problemTitle = problemTitle;
            return this;
        }

        @Generated
        public ContestSubmissionRequestBuilder timeFromStartMs(Long timeFromStartMs) {
            this.timeFromStartMs = timeFromStartMs;
            return this;
        }

        @Generated
        public ContestSubmissionRequest build() {
            return new ContestSubmissionRequest(this.contestCode, this.contestId, this.userId, this.userName, this.problemId, this.problemTitle, this.code, this.language, this.timeFromStartMs);
        }

        @Generated
        public String toString() {
            return "ContestSubmissionRequest.ContestSubmissionRequestBuilder(contestCode=" + this.contestCode + ", problemId=" + this.problemId + ", code=" + this.code + ", language=" + this.language + ")";
        }
    }
}
