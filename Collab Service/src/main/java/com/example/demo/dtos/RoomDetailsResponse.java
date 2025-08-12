/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package com.example.demo.dtos;

import com.example.demo.dtos.ParticipantDto;
import com.example.demo.dtos.ProblemDetailsDto;
import java.util.List;
import lombok.Generated;

public class RoomDetailsResponse {
    private String roomCode;
    private String roomName;
    private List<ParticipantDto> participants;
    private Integer participantCount;
    private String currentCode;
    private String language;
    private Long timestamp;
    private Boolean voiceEnabled;
    private List<ProblemDetailsDto> problems;

    @Generated
    public static RoomDetailsResponseBuilder builder() {
        return new RoomDetailsResponseBuilder();
    }

    @Generated
    public String getRoomCode() {
        return this.roomCode;
    }

    @Generated
    public String getRoomName() {
        return this.roomName;
    }

    @Generated
    public List<ParticipantDto> getParticipants() {
        return this.participants;
    }

    @Generated
    public Integer getParticipantCount() {
        return this.participantCount;
    }

    @Generated
    public String getCurrentCode() {
        return this.currentCode;
    }

    @Generated
    public String getLanguage() {
        return this.language;
    }

    @Generated
    public Long getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public Boolean getVoiceEnabled() {
        return this.voiceEnabled;
    }

    @Generated
    public List<ProblemDetailsDto> getProblems() {
        return this.problems;
    }

    @Generated
    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    @Generated
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Generated
    public void setParticipants(List<ParticipantDto> participants) {
        this.participants = participants;
    }

    @Generated
    public void setParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
    }

    @Generated
    public void setCurrentCode(String currentCode) {
        this.currentCode = currentCode;
    }

    @Generated
    public void setLanguage(String language) {
        this.language = language;
    }

    @Generated
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public void setVoiceEnabled(Boolean voiceEnabled) {
        this.voiceEnabled = voiceEnabled;
    }

    @Generated
    public void setProblems(List<ProblemDetailsDto> problems) {
        this.problems = problems;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoomDetailsResponse)) {
            return false;
        }
        RoomDetailsResponse other = (RoomDetailsResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$participantCount = this.getParticipantCount();
        Integer other$participantCount = other.getParticipantCount();
        if (this$participantCount == null ? other$participantCount != null : !((Object)this$participantCount).equals(other$participantCount)) {
            return false;
        }
        Long this$timestamp = this.getTimestamp();
        Long other$timestamp = other.getTimestamp();
        if (this$timestamp == null ? other$timestamp != null : !((Object)this$timestamp).equals(other$timestamp)) {
            return false;
        }
        Boolean this$voiceEnabled = this.getVoiceEnabled();
        Boolean other$voiceEnabled = other.getVoiceEnabled();
        if (this$voiceEnabled == null ? other$voiceEnabled != null : !((Object)this$voiceEnabled).equals(other$voiceEnabled)) {
            return false;
        }
        String this$roomCode = this.getRoomCode();
        String other$roomCode = other.getRoomCode();
        if (this$roomCode == null ? other$roomCode != null : !this$roomCode.equals(other$roomCode)) {
            return false;
        }
        String this$roomName = this.getRoomName();
        String other$roomName = other.getRoomName();
        if (this$roomName == null ? other$roomName != null : !this$roomName.equals(other$roomName)) {
            return false;
        }
        List<ParticipantDto> this$participants = this.getParticipants();
        List<ParticipantDto> other$participants = other.getParticipants();
        if (this$participants == null ? other$participants != null : !((Object)this$participants).equals(other$participants)) {
            return false;
        }
        String this$currentCode = this.getCurrentCode();
        String other$currentCode = other.getCurrentCode();
        if (this$currentCode == null ? other$currentCode != null : !this$currentCode.equals(other$currentCode)) {
            return false;
        }
        String this$language = this.getLanguage();
        String other$language = other.getLanguage();
        if (this$language == null ? other$language != null : !this$language.equals(other$language)) {
            return false;
        }
        List<ProblemDetailsDto> this$problems = this.getProblems();
        List<ProblemDetailsDto> other$problems = other.getProblems();
        return !(this$problems == null ? other$problems != null : !((Object)this$problems).equals(other$problems));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof RoomDetailsResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $participantCount = this.getParticipantCount();
        result = result * 59 + ($participantCount == null ? 43 : ((Object)$participantCount).hashCode());
        Long $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : ((Object)$timestamp).hashCode());
        Boolean $voiceEnabled = this.getVoiceEnabled();
        result = result * 59 + ($voiceEnabled == null ? 43 : ((Object)$voiceEnabled).hashCode());
        String $roomCode = this.getRoomCode();
        result = result * 59 + ($roomCode == null ? 43 : $roomCode.hashCode());
        String $roomName = this.getRoomName();
        result = result * 59 + ($roomName == null ? 43 : $roomName.hashCode());
        List<ParticipantDto> $participants = this.getParticipants();
        result = result * 59 + ($participants == null ? 43 : ((Object)$participants).hashCode());
        String $currentCode = this.getCurrentCode();
        result = result * 59 + ($currentCode == null ? 43 : $currentCode.hashCode());
        String $language = this.getLanguage();
        result = result * 59 + ($language == null ? 43 : $language.hashCode());
        List<ProblemDetailsDto> $problems = this.getProblems();
        result = result * 59 + ($problems == null ? 43 : ((Object)$problems).hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "RoomDetailsResponse(roomCode=" + this.getRoomCode() + ", roomName=" + this.getRoomName() + ", participants=" + String.valueOf(this.getParticipants()) + ", participantCount=" + this.getParticipantCount() + ", currentCode=" + this.getCurrentCode() + ", language=" + this.getLanguage() + ", timestamp=" + this.getTimestamp() + ", voiceEnabled=" + this.getVoiceEnabled() + ", problems=" + String.valueOf(this.getProblems()) + ")";
    }

    @Generated
    public RoomDetailsResponse() {
    }

    @Generated
    public RoomDetailsResponse(String roomCode, String roomName, List<ParticipantDto> participants, Integer participantCount, String currentCode, String language, Long timestamp, Boolean voiceEnabled, List<ProblemDetailsDto> problems) {
        this.roomCode = roomCode;
        this.roomName = roomName;
        this.participants = participants;
        this.participantCount = participantCount;
        this.currentCode = currentCode;
        this.language = language;
        this.timestamp = timestamp;
        this.voiceEnabled = voiceEnabled;
        this.problems = problems;
    }

    @Generated
    public static class RoomDetailsResponseBuilder {
        @Generated
        private String roomCode;
        @Generated
        private String roomName;
        @Generated
        private List<ParticipantDto> participants;
        @Generated
        private Integer participantCount;
        @Generated
        private String currentCode;
        @Generated
        private String language;
        @Generated
        private Long timestamp;
        @Generated
        private Boolean voiceEnabled;
        @Generated
        private List<ProblemDetailsDto> problems;

        @Generated
        RoomDetailsResponseBuilder() {
        }

        @Generated
        public RoomDetailsResponseBuilder roomCode(String roomCode) {
            this.roomCode = roomCode;
            return this;
        }

        @Generated
        public RoomDetailsResponseBuilder roomName(String roomName) {
            this.roomName = roomName;
            return this;
        }

        @Generated
        public RoomDetailsResponseBuilder participants(List<ParticipantDto> participants) {
            this.participants = participants;
            return this;
        }

        @Generated
        public RoomDetailsResponseBuilder participantCount(Integer participantCount) {
            this.participantCount = participantCount;
            return this;
        }

        @Generated
        public RoomDetailsResponseBuilder currentCode(String currentCode) {
            this.currentCode = currentCode;
            return this;
        }

        @Generated
        public RoomDetailsResponseBuilder language(String language) {
            this.language = language;
            return this;
        }

        @Generated
        public RoomDetailsResponseBuilder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Generated
        public RoomDetailsResponseBuilder voiceEnabled(Boolean voiceEnabled) {
            this.voiceEnabled = voiceEnabled;
            return this;
        }

        @Generated
        public RoomDetailsResponseBuilder problems(List<ProblemDetailsDto> problems) {
            this.problems = problems;
            return this;
        }

        @Generated
        public RoomDetailsResponse build() {
            return new RoomDetailsResponse(this.roomCode, this.roomName, this.participants, this.participantCount, this.currentCode, this.language, this.timestamp, this.voiceEnabled, this.problems);
        }

        @Generated
        public String toString() {
            return "RoomDetailsResponse.RoomDetailsResponseBuilder(roomCode=" + this.roomCode + ", roomName=" + this.roomName + ", participants=" + String.valueOf(this.participants) + ", participantCount=" + this.participantCount + ", currentCode=" + this.currentCode + ", language=" + this.language + ", timestamp=" + this.timestamp + ", voiceEnabled=" + this.voiceEnabled + ", problems=" + String.valueOf(this.problems) + ")";
        }
    }
}
