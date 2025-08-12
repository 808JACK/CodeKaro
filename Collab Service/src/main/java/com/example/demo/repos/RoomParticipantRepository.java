/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Modifying
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 *  org.springframework.transaction.annotation.Transactional
 */
package com.example.demo.repos;

import com.example.demo.entities.RoomParticipants;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RoomParticipantRepository
extends JpaRepository<RoomParticipants, Long> {
    public Optional<RoomParticipants> findByRoomIdAndUserId(Long var1, Long var2);

    public List<RoomParticipants> findByRoomIdAndIsActiveTrue(Long var1);

    public List<RoomParticipants> findByUserId(Long var1);

    public boolean existsByRoomIdAndUserId(Long var1, Long var2);

    @Modifying
    @Transactional
    @Query(value="UPDATE RoomParticipants rp SET rp.isActive = :isActive, rp.lastSeenAt = :lastSeenAt WHERE rp.roomId = :roomId AND rp.userId = :userId")
    public void updateParticipantStatus(@Param(value="roomId") Long var1, @Param(value="userId") Long var2, @Param(value="isActive") Boolean var3, @Param(value="lastSeenAt") ZonedDateTime var4);

    @Modifying
    @Transactional
    @Query(value="INSERT INTO room_participants (room_id, user_id, username, role, is_active, joined_at) VALUES (:roomId, :userId, :username, :role, :isActive, :joinedAt)", nativeQuery=true)
    public void insertParticipant(@Param(value="roomId") Long var1, @Param(value="userId") Long var2, @Param(value="username") String var3, @Param(value="role") String var4, @Param(value="isActive") Boolean var5, @Param(value="joinedAt") ZonedDateTime var6);

    public List<RoomParticipants> findByRoomIdAndIsActive(Long var1, Boolean var2);

    public long countByRoomIdAndIsActive(Long var1, Boolean var2);

    public List<RoomParticipants> findByRoomId(Long var1);
}
