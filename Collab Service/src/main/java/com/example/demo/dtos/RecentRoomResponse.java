package com.example.demo.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
@Builder
public class RecentRoomResponse {
    private String id;
    private String name;
    private String type; // "contest" or "room"
    private Integer participants;
    private String lastActive;
    private ZonedDateTime createdAt;
}