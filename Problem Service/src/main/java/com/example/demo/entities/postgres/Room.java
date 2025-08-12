package com.example.demo.entities.postgres;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "problem_id")
    private Long problemId;

    @Column(name = "creator_user_id", nullable = false)
    private Long creatorUserId;

    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "last_active_at", nullable = false)
    private ZonedDateTime lastActiveAt;

    @Column(name = "invite_code", unique = true)
    private String inviteCode;

    @Column(name = "current_code_doc_id", nullable = false)
    private String currentCodeDocId;  // References MongoDB RoomCompactedCodeStates._id
} 