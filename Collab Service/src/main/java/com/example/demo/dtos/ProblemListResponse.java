/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.util.List;
import lombok.Generated;

class ProblemListResponse {
    private Long id;
    private String title;
    private String difficulty;
    private List<Long> topicIds;

    @Generated
    public static ProblemListResponseBuilder builder() {
        return new ProblemListResponseBuilder();
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
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ProblemListResponse)) {
            return false;
        }
        ProblemListResponse other = (ProblemListResponse)o;
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
        return !(this$topicIds == null ? other$topicIds != null : !((Object)this$topicIds).equals(other$topicIds));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ProblemListResponse;
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
        return result;
    }

    @Generated
    public String toString() {
        return "ProblemListResponse(id=" + this.getId() + ", title=" + this.getTitle() + ", difficulty=" + this.getDifficulty() + ", topicIds=" + String.valueOf(this.getTopicIds()) + ")";
    }

    @Generated
    public ProblemListResponse() {
    }

    @Generated
    public ProblemListResponse(Long id, String title, String difficulty, List<Long> topicIds) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.topicIds = topicIds;
    }

    @Generated
    public static class ProblemListResponseBuilder {
        @Generated
        private Long id;
        @Generated
        private String title;
        @Generated
        private String difficulty;
        @Generated
        private List<Long> topicIds;

        @Generated
        ProblemListResponseBuilder() {
        }

        @Generated
        public ProblemListResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ProblemListResponseBuilder title(String title) {
            this.title = title;
            return this;
        }

        @Generated
        public ProblemListResponseBuilder difficulty(String difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        @Generated
        public ProblemListResponseBuilder topicIds(List<Long> topicIds) {
            this.topicIds = topicIds;
            return this;
        }

        @Generated
        public ProblemListResponse build() {
            return new ProblemListResponse(this.id, this.title, this.difficulty, this.topicIds);
        }

        @Generated
        public String toString() {
            return "ProblemListResponse.ProblemListResponseBuilder(id=" + this.id + ", title=" + this.title + ", difficulty=" + this.difficulty + ", topicIds=" + String.valueOf(this.topicIds) + ")";
        }
    }
}
