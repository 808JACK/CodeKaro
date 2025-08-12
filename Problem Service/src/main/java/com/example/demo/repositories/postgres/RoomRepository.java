package com.example.demo.repositories.postgres;

import com.example.demo.entities.postgres.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByInviteCode(String inviteCode);
    Optional<Room> findByProblemIdAndCreatorUserId(Long problemId, Long creatorUserId);
} 