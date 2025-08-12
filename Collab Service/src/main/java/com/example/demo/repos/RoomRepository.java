/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.example.demo.repos;

import com.example.demo.entities.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository
extends JpaRepository<Room, Long> {
    public Optional<Room> findByInviteCode(String var1);

    public boolean existsByInviteCode(String var1);

    @Query(value="SELECT problem_id FROM room_problems WHERE room_id = :roomId", nativeQuery=true)
    public List<Long> findProblemIdsByRoomId(@Param(value="roomId") Long var1);
}
