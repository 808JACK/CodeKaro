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

import com.example.demo.entities.RoomCodeState;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomCodeStateRepository
extends JpaRepository<RoomCodeState, Long> {
    public Optional<RoomCodeState> findByRoomId(Long var1);

    public boolean existsByRoomId(Long var1);

    public void deleteByRoomId(Long var1);

    @Query(value="SELECT r.finalCode FROM RoomCodeState r WHERE r.roomId = :roomId")
    public Optional<String> getFinalCodeByRoomId(@Param(value="roomId") Long var1);
}
