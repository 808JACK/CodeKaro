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
    private Long problemId;
    private String code;
    private String language;

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
    public ContestSubmissionRequest(String contestCode, Long problemId, String code, String language) {
        this.contestCode = contestCode;
        this.problemId = problemId;
        this.code = code;
        this.language = language;
    }

    @Generated
    public static class ContestSubmissionRequestBuilder {
        @Generated
        private String contestCode;
        @Generated
        private Long problemId;
        @Generated
        private String code;
        @Generated
        private String language;

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
        public ContestSubmissionRequest build() {
            return new ContestSubmissionRequest(this.contestCode, this.problemId, this.code, this.language);
        }

        @Generated
        public String toString() {
            return "ContestSubmissionRequest.ContestSubmissionRequestBuilder(contestCode=" + this.contestCode + ", problemId=" + this.problemId + ", code=" + this.code + ", language=" + this.language + ")";
        }
    }
}
