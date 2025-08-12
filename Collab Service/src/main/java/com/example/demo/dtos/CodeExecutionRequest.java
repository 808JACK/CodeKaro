/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import lombok.Generated;

public class CodeExecutionRequest {
    private String code;
    private String language;
    private Long problemId;
    private String problemName;
    private long timeLimitMs = 5000L;
    private long memoryLimitMb = 256L;

    @Generated
    public CodeExecutionRequest() {
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
    public Long getProblemId() {
        return this.problemId;
    }

    @Generated
    public String getProblemName() {
        return this.problemName;
    }

    @Generated
    public long getTimeLimitMs() {
        return this.timeLimitMs;
    }

    @Generated
    public long getMemoryLimitMb() {
        return this.memoryLimitMb;
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
    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    @Generated
    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    @Generated
    public void setTimeLimitMs(long timeLimitMs) {
        this.timeLimitMs = timeLimitMs;
    }

    @Generated
    public void setMemoryLimitMb(long memoryLimitMb) {
        this.memoryLimitMb = memoryLimitMb;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CodeExecutionRequest)) {
            return false;
        }
        CodeExecutionRequest other = (CodeExecutionRequest)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getTimeLimitMs() != other.getTimeLimitMs()) {
            return false;
        }
        if (this.getMemoryLimitMb() != other.getMemoryLimitMb()) {
            return false;
        }
        Long this$problemId = this.getProblemId();
        Long other$problemId = other.getProblemId();
        if (this$problemId == null ? other$problemId != null : !((Object)this$problemId).equals(other$problemId)) {
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
        String this$problemName = this.getProblemName();
        String other$problemName = other.getProblemName();
        return !(this$problemName == null ? other$problemName != null : !this$problemName.equals(other$problemName));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodeExecutionRequest;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $timeLimitMs = this.getTimeLimitMs();
        result = result * 59 + (int)($timeLimitMs >>> 32 ^ $timeLimitMs);
        long $memoryLimitMb = this.getMemoryLimitMb();
        result = result * 59 + (int)($memoryLimitMb >>> 32 ^ $memoryLimitMb);
        Long $problemId = this.getProblemId();
        result = result * 59 + ($problemId == null ? 43 : ((Object)$problemId).hashCode());
        String $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        String $problemName = this.getProblemName();
        result = result * 59 + ($problemName == null ? 43 : $problemName.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CodeExecutionRequest(code=" + this.getCode() + ", language=" + this.getLanguage() + ", problemId=" + this.getProblemId() + ", problemName=" + this.getProblemName() + ", timeLimitMs=" + this.getTimeLimitMs() + ", memoryLimitMb=" + this.getMemoryLimitMb() + ")";
    }
}
