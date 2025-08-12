/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.util.List;
import lombok.Generated;

public class ProblemResponse {
    private Long id;
    private String title;
    private String difficulty;
    private List<Long> topicIds;
    private String content;
    private String constraints;
    private String inputFormat;
    private String outputFormat;
    private List<Example> examples;
    private String template;
    private String functionName;
    private String methodSignature;
    private List<String> supportedLanguages;

    @Generated
    public static ProblemResponseBuilder builder() {
        return new ProblemResponseBuilder();
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getDifficulty() {
        return this.difficulty;
    }

    @Generated
    public List<Long> getTopicIds() {
        return this.topicIds;
    }

    @Generated
    public String getContent() {
        return this.content;
    }

    @Generated
    public String getConstraints() {
        return this.constraints;
    }

    @Generated
    public String getInputFormat() {
        return this.inputFormat;
    }

    @Generated
    public String getOutputFormat() {
        return this.outputFormat;
    }

    @Generated
    public List<Example> getExamples() {
        return this.examples;
    }

    @Generated
    public String getTemplate() {
        return this.template;
    }

    @Generated
    public String getFunctionName() {
        return this.functionName;
    }

    @Generated
    public String getMethodSignature() {
        return this.methodSignature;
    }

    @Generated
    public List<String> getSupportedLanguages() {
        return this.supportedLanguages;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setTitle(String title) {
        this.title = title;
    }

    @Generated
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Generated
    public void setTopicIds(List<Long> topicIds) {
        this.topicIds = topicIds;
    }

    @Generated
    public void setContent(String content) {
        this.content = content;
    }

    @Generated
    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    @Generated
    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    @Generated
    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    @Generated
    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    @Generated
    public void setTemplate(String template) {
        this.template = template;
    }

    @Generated
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    @Generated
    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    @Generated
    public void setSupportedLanguages(List<String> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProblemResponse)) {
            return false;
        }
        ProblemResponse other = (ProblemResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$id = this.getId();
        Long other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$difficulty = this.getDifficulty();
        String other$difficulty = other.getDifficulty();
        if (this$difficulty == null ? other$difficulty != null : !this$difficulty.equals(other$difficulty)) {
            return false;
        }
        List<Long> this$topicIds = this.getTopicIds();
        List<Long> other$topicIds = other.getTopicIds();
        if (this$topicIds == null ? other$topicIds != null : !((Object)this$topicIds).equals(other$topicIds)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        if (this$content == null ? other$content != null : !this$content.equals(other$content)) {
            return false;
        }
        String this$constraints = this.getConstraints();
        String other$constraints = other.getConstraints();
        if (this$constraints == null ? other$constraints != null : !this$constraints.equals(other$constraints)) {
            return false;
        }
        String this$inputFormat = this.getInputFormat();
        String other$inputFormat = other.getInputFormat();
        if (this$inputFormat == null ? other$inputFormat != null : !this$inputFormat.equals(other$inputFormat)) {
            return false;
        }
        String this$outputFormat = this.getOutputFormat();
        String other$outputFormat = other.getOutputFormat();
        if (this$outputFormat == null ? other$outputFormat != null : !this$outputFormat.equals(other$outputFormat)) {
            return false;
        }
        List<Example> this$examples = this.getExamples();
        List<Example> other$examples = other.getExamples();
        if (this$examples == null ? other$examples != null : !((Object)this$examples).equals(other$examples)) {
            return false;
        }
        String this$template = this.getTemplate();
        String other$template = other.getTemplate();
        if (this$template == null ? other$template != null : !this$template.equals(other$template)) {
            return false;
        }
        String this$functionName = this.getFunctionName();
        String other$functionName = other.getFunctionName();
        if (this$functionName == null ? other$functionName != null : !this$functionName.equals(other$functionName)) {
            return false;
        }
        String this$methodSignature = this.getMethodSignature();
        String other$methodSignature = other.getMethodSignature();
        if (this$methodSignature == null ? other$methodSignature != null : !this$methodSignature.equals(other$methodSignature)) {
            return false;
        }
        List<String> this$supportedLanguages = this.getSupportedLanguages();
        List<String> other$supportedLanguages = other.getSupportedLanguages();
        return !(this$supportedLanguages == null ? other$supportedLanguages != null : !((Object)this$supportedLanguages).equals(other$supportedLanguages));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ProblemResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $difficulty = this.getDifficulty();
        result = result * 59 + ($difficulty == null ? 43 : $difficulty.hashCode());
        List<Long> $topicIds = this.getTopicIds();
        result = result * 59 + ($topicIds == null ? 43 : ((Object)$topicIds).hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        String $constraints = this.getConstraints();
        result = result * 59 + ($constraints == null ? 43 : $constraints.hashCode());
        String $inputFormat = this.getInputFormat();
        result = result * 59 + ($inputFormat == null ? 43 : $inputFormat.hashCode());
        String $outputFormat = this.getOutputFormat();
        result = result * 59 + ($outputFormat == null ? 43 : $outputFormat.hashCode());
        List<Example> $examples = this.getExamples();
        result = result * 59 + ($examples == null ? 43 : ((Object)$examples).hashCode());
        String $template = this.getTemplate();
        result = result * 59 + ($template == null ? 43 : $template.hashCode());
        String $functionName = this.getFunctionName();
        result = result * 59 + ($functionName == null ? 43 : $functionName.hashCode());
        String $methodSignature = this.getMethodSignature();
        result = result * 59 + ($methodSignature == null ? 43 : $methodSignature.hashCode());
        List<String> $supportedLanguages = this.getSupportedLanguages();
        result = result * 59 + ($supportedLanguages == null ? 43 : ((Object)$supportedLanguages).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ProblemResponse(id=" + this.getId() + ", title=" + this.getTitle() + ", difficulty=" + this.getDifficulty() + ", topicIds=" + String.valueOf(this.getTopicIds()) + ", content=" + this.getContent() + ", constraints=" + this.getConstraints() + ", inputFormat=" + this.getInputFormat() + ", outputFormat=" + this.getOutputFormat() + ", examples=" + String.valueOf(this.getExamples()) + ", template=" + this.getTemplate() + ", functionName=" + this.getFunctionName() + ", methodSignature=" + this.getMethodSignature() + ", supportedLanguages=" + String.valueOf(this.getSupportedLanguages()) + ")";
    }

    @Generated
    public ProblemResponse() {
    }

    @Generated
    public ProblemResponse(Long id, String title, String difficulty, List<Long> topicIds, String content, String constraints, String inputFormat, String outputFormat, List<Example> examples, String template, String functionName, String methodSignature, List<String> supportedLanguages) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.topicIds = topicIds;
        this.content = content;
        this.constraints = constraints;
        this.inputFormat = inputFormat;
        this.outputFormat = outputFormat;
        this.examples = examples;
        this.template = template;
        this.functionName = functionName;
        this.methodSignature = methodSignature;
        this.supportedLanguages = supportedLanguages;
    }

    @Generated
    public static class ProblemResponseBuilder {
        @Generated
        private Long id;
        @Generated
        private String title;
        @Generated
        private String difficulty;
        @Generated
        private List<Long> topicIds;
        @Generated
        private String content;
        @Generated
        private String constraints;
        @Generated
        private String inputFormat;
        @Generated
        private String outputFormat;
        @Generated
        private List<Example> examples;
        @Generated
        private String template;
        @Generated
        private String functionName;
        @Generated
        private String methodSignature;
        @Generated
        private List<String> supportedLanguages;

        @Generated
        ProblemResponseBuilder() {
        }

        @Generated
        public ProblemResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ProblemResponseBuilder title(String title) {
            this.title = title;
            return this;
        }

        @Generated
        public ProblemResponseBuilder difficulty(String difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        @Generated
        public ProblemResponseBuilder topicIds(List<Long> topicIds) {
            this.topicIds = topicIds;
            return this;
        }

        @Generated
        public ProblemResponseBuilder content(String content) {
            this.content = content;
            return this;
        }

        @Generated
        public ProblemResponseBuilder constraints(String constraints) {
            this.constraints = constraints;
            return this;
        }

        @Generated
        public ProblemResponseBuilder inputFormat(String inputFormat) {
            this.inputFormat = inputFormat;
            return this;
        }

        @Generated
        public ProblemResponseBuilder outputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        @Generated
        public ProblemResponseBuilder examples(List<Example> examples) {
            this.examples = examples;
            return this;
        }

        @Generated
        public ProblemResponseBuilder template(String template) {
            this.template = template;
            return this;
        }

        @Generated
        public ProblemResponseBuilder functionName(String functionName) {
            this.functionName = functionName;
            return this;
        }

        @Generated
        public ProblemResponseBuilder methodSignature(String methodSignature) {
            this.methodSignature = methodSignature;
            return this;
        }

        @Generated
        public ProblemResponseBuilder supportedLanguages(List<String> supportedLanguages) {
            this.supportedLanguages = supportedLanguages;
            return this;
        }

        @Generated
        public ProblemResponse build() {
            return new ProblemResponse(this.id, this.title, this.difficulty, this.topicIds, this.content, this.constraints, this.inputFormat, this.outputFormat, this.examples, this.template, this.functionName, this.methodSignature, this.supportedLanguages);
        }

        @Generated
        public String toString() {
            return "ProblemResponse.ProblemResponseBuilder(id=" + this.id + ", title=" + this.title + ", difficulty=" + this.difficulty + ", topicIds=" + String.valueOf(this.topicIds) + ", content=" + this.content + ", constraints=" + this.constraints + ", inputFormat=" + this.inputFormat + ", outputFormat=" + this.outputFormat + ", examples=" + String.valueOf(this.examples) + ", template=" + this.template + ", functionName=" + this.functionName + ", methodSignature=" + this.methodSignature + ", supportedLanguages=" + String.valueOf(this.supportedLanguages) + ")";
        }
    }

    public static class Example {
        private String input;
        private String output;

        @Generated
        public static ExampleBuilder builder() {
            return new ExampleBuilder();
        }

        @Generated
        public String getInput() {
            return this.input;
        }

        @Generated
        public String getOutput() {
            return this.output;
        }

        @Generated
        public void setInput(String input) {
            this.input = input;
        }

        @Generated
        public void setOutput(String output) {
            this.output = output;
        }

        @Generated
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Example)) {
                return false;
            }
            Example other = (Example)o;
            if (!other.canEqual(this)) {
                return false;
            }
            String this$input = this.getInput();
            String other$input = other.getInput();
            if (this$input == null ? other$input != null : !this$input.equals(other$input)) {
                return false;
            }
            String this$output = this.getOutput();
            String other$output = other.getOutput();
            return !(this$output == null ? other$output != null : !this$output.equals(other$output));
        }

        @Generated
        protected boolean canEqual(Object other) {
            return other instanceof Example;
        }

        @Generated
        public int hashCode() {
            int PRIME = 59;
            int result = 1;
            String $input = this.getInput();
            result = result * 59 + ($input == null ? 43 : $input.hashCode());
            String $output = this.getOutput();
            result = result * 59 + ($output == null ? 43 : $output.hashCode());
            return result;
        }

        @Generated
        public String toString() {
            return "ProblemResponse.Example(input=" + this.getInput() + ", output=" + this.getOutput() + ")";
        }

        @Generated
        public Example() {
        }

        @Generated
        public Example(String input, String output) {
            this.input = input;
            this.output = output;
        }

        @Generated
        public static class ExampleBuilder {
            @Generated
            private String input;
            @Generated
            private String output;

            @Generated
            ExampleBuilder() {
            }

            @Generated
            public ExampleBuilder input(String input) {
                this.input = input;
                return this;
            }

            @Generated
            public ExampleBuilder output(String output) {
                this.output = output;
                return this;
            }

            @Generated
            public Example build() {
                return new Example(this.input, this.output);
            }

            @Generated
            public String toString() {
                return "ProblemResponse.Example.ExampleBuilder(input=" + this.input + ", output=" + this.output + ")";
            }
        }
    }
}
