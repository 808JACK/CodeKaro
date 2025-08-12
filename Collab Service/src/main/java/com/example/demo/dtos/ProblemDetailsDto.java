/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import java.util.Arrays;
import lombok.Generated;

public class ProblemDetailsDto {
    private Long id;
    private String title;
    private String description;
    private String difficulty;
    private String category;
    private String[] tags;
    private String templateCode;
    private String language;

    @Generated
    public static ProblemDetailsDtoBuilder builder() {
        return new ProblemDetailsDtoBuilder();
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
    public String getDescription() {
        return this.description;
    }

    @Generated
    public String getDifficulty() {
        return this.difficulty;
    }

    @Generated
    public String getCategory() {
        return this.category;
    }

    @Generated
    public String[] getTags() {
        return this.tags;
    }

    @Generated
    public String getTemplateCode() {
        return this.templateCode;
    }

    @Generated
    public String getLanguage() {
        return this.language;
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
    public void setDescription(String description) {
        this.description = description;
    }

    @Generated
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Generated
    public void setCategory(String category) {
        this.category = category;
    }

    @Generated
    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Generated
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
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
        if (!(o instanceof ProblemDetailsDto)) {
            return false;
        }
        ProblemDetailsDto other = (ProblemDetailsDto)o;
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
        String this$description = this.getDescription();
        String other$description = other.getDescription();
        if (this$description == null ? other$description != null : !this$description.equals(other$description)) {
            return false;
        }
        String this$difficulty = this.getDifficulty();
        String other$difficulty = other.getDifficulty();
        if (this$difficulty == null ? other$difficulty != null : !this$difficulty.equals(other$difficulty)) {
            return false;
        }
        String this$category = this.getCategory();
        String other$category = other.getCategory();
        if (this$category == null ? other$category != null : !this$category.equals(other$category)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getTags(), other.getTags())) {
            return false;
        }
        String this$templateCode = this.getTemplateCode();
        String other$templateCode = other.getTemplateCode();
        if (this$templateCode == null ? other$templateCode != null : !this$templateCode.equals(other$templateCode)) {
            return false;
        }
        String this$language = this.getLanguage();
        String other$language = other.getLanguage();
        return !(this$language == null ? other$language != null : !this$language.equals(other$language));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ProblemDetailsDto;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $description = this.getDescription();
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        String $difficulty = this.getDifficulty();
        result = result * 59 + ($difficulty == null ? 43 : $difficulty.hashCode());
        String $category = this.getCategory();
        result = result * 59 + ($category == null ? 43 : $category.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getTags());
        String $templateCode = this.getTemplateCode();
        result = result * 59 + ($templateCode == null ? 43 : $templateCode.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ProblemDetailsDto(id=" + this.getId() + ", title=" + this.getTitle() + ", description=" + this.getDescription() + ", difficulty=" + this.getDifficulty() + ", category=" + this.getCategory() + ", tags=" + Arrays.deepToString(this.getTags()) + ", templateCode=" + this.getTemplateCode() + ", language=" + this.getLanguage() + ")";
    }

    @Generated
    public ProblemDetailsDto() {
    }

    @Generated
    public ProblemDetailsDto(Long id, String title, String description, String difficulty, String category, String[] tags, String templateCode, String language) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
        this.tags = tags;
        this.templateCode = templateCode;
        this.language = language;
    }

    @Generated
    public static class ProblemDetailsDtoBuilder {
        @Generated
        private Long id;
        @Generated
        private String title;
        @Generated
        private String description;
        @Generated
        private String difficulty;
        @Generated
        private String category;
        @Generated
        private String[] tags;
        @Generated
        private String templateCode;
        @Generated
        private String language;

        @Generated
        ProblemDetailsDtoBuilder() {
        }

        @Generated
        public ProblemDetailsDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        @Generated
        public ProblemDetailsDtoBuilder title(String title) {
            this.title = title;
            return this;
        }

        @Generated
        public ProblemDetailsDtoBuilder description(String description) {
            this.description = description;
            return this;
        }

        @Generated
        public ProblemDetailsDtoBuilder difficulty(String difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        @Generated
        public ProblemDetailsDtoBuilder category(String category) {
            this.category = category;
            return this;
        }

        @Generated
        public ProblemDetailsDtoBuilder tags(String[] tags) {
            this.tags = tags;
            return this;
        }

        @Generated
        public ProblemDetailsDtoBuilder templateCode(String templateCode) {
            this.templateCode = templateCode;
            return this;
        }

        @Generated
        public ProblemDetailsDtoBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public ProblemDetailsDto build() {
            return new ProblemDetailsDto(this.id, this.title, this.description, this.difficulty, this.category, this.tags, this.templateCode, this.language);
        }

        @Generated
        public String toString() {
            return "ProblemDetailsDto.ProblemDetailsDtoBuilder(id=" + this.id + ", title=" + this.title + ", description=" + this.description + ", difficulty=" + this.difficulty + ", category=" + this.category + ", tags=" + Arrays.deepToString(this.tags) + ", templateCode=" + this.templateCode + ", language=" + this.language + ")";
        }
    }
}
