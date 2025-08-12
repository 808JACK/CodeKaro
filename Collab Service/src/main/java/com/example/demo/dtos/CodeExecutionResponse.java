/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.util.List;
import lombok.Generated;

public class CodeExecutionResponse {
    private String overallVerdict;
    private List<TestResult> testResults;
    private String error;

    @Generated
    public CodeExecutionResponse() {
    }

    @Generated
    public String getOverallVerdict() {
        return this.overallVerdict;
    }

    @Generated
    public List<TestResult> getTestResults() {
        return this.testResults;
    }

    @Generated
    public String getError() {
        return this.error;
    }

    @Generated
    public void setOverallVerdict(String overallVerdict) {
        this.overallVerdict = overallVerdict;
    }

    @Generated
    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    @Generated
    public void setError(String error) {
        this.error = error;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CodeExecutionResponse)) {
            return false;
        }
        CodeExecutionResponse other = (CodeExecutionResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        String this$overallVerdict = this.getOverallVerdict();
        String other$overallVerdict = other.getOverallVerdict();
        if (this$overallVerdict == null ? other$overallVerdict != null : !this$overallVerdict.equals(other$overallVerdict)) {
            return false;
        }
        List<TestResult> this$testResults = this.getTestResults();
        List<TestResult> other$testResults = other.getTestResults();
        if (this$testResults == null ? other$testResults != null : !((Object)this$testResults).equals(other$testResults)) {
            return false;
        }
        String this$error = this.getError();
        String other$error = other.getError();
        return !(this$error == null ? other$error != null : !this$error.equals(other$error));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof CodeExecutionResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        String $overallVerdict = this.getOverallVerdict();
        result = result * 59 + ($overallVerdict == null ? 43 : $overallVerdict.hashCode());
        List<TestResult> $testResults = this.getTestResults();
        result = result * 59 + ($testResults == null ? 43 : ((Object)$testResults).hashCode());
        String $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "CodeExecutionResponse(overallVerdict=" + this.getOverallVerdict() + ", testResults=" + String.valueOf(this.getTestResults()) + ", error=" + this.getError() + ")";
    }

    public static class TestResult {
        private int testNumber;
        private String verdict;
        private long timeMs;
        private String output;
        private String error;

        @Generated
        public TestResult() {
        }

        @Generated
        public int getTestNumber() {
            return this.testNumber;
        }

        @Generated
        public String getVerdict() {
            return this.verdict;
        }

        @Generated
        public long getTimeMs() {
            return this.timeMs;
        }

        @Generated
        public String getOutput() {
            return this.output;
        }

        @Generated
        public String getError() {
            return this.error;
        }

        @Generated
        public void setTestNumber(int testNumber) {
            this.testNumber = testNumber;
        }

        @Generated
        public void setVerdict(String verdict) {
            this.verdict = verdict;
        }

        @Generated
        public void setTimeMs(long timeMs) {
            this.timeMs = timeMs;
        }

        @Generated
        public void setOutput(String output) {
            this.output = output;
        }

        @Generated
        public void setError(String error) {
            this.error = error;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof TestResult)) {
                return false;
            }
            TestResult other = (TestResult)o;
            if (!other.canEqual(this)) {
                return false;
            }
            if (this.getTestNumber() != other.getTestNumber()) {
                return false;
            }
            if (this.getTimeMs() != other.getTimeMs()) {
                return false;
            }
            String this$verdict = this.getVerdict();
            String other$verdict = other.getVerdict();
            if (this$verdict == null ? other$verdict != null : !this$verdict.equals(other$verdict)) {
                return false;
            }
            String this$output = this.getOutput();
            String other$output = other.getOutput();
            if (this$output == null ? other$output != null : !this$output.equals(other$output)) {
                return false;
            }
            String this$error = this.getError();
            String other$error = other.getError();
            return !(this$error == null ? other$error != null : !this$error.equals(other$error));
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof TestResult;
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            result = result * 59 + this.getTestNumber();
            long $timeMs = this.getTimeMs();
            result = result * 59 + (int)($timeMs >>> 32 ^ $timeMs);
            String $verdict = this.getVerdict();
            result = result * 59 + ($verdict == null ? 43 : $verdict.hashCode());
            String $output = this.getOutput();
            result = result * 59 + ($output == null ? 43 : $output.hashCode());
            String $error = this.getError();
            result = result * 59 + ($error == null ? 43 : $error.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "CodeExecutionResponse.TestResult(testNumber=" + this.getTestNumber() + ", verdict=" + this.getVerdict() + ", timeMs=" + this.getTimeMs() + ", output=" + this.getOutput() + ", error=" + this.getError() + ")";
        }
    }
}
