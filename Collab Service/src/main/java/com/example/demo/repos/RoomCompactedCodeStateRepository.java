/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.example.demo.repos;

import com.example.demo.entities.RoomCompactedCodeState;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// @Repository - Disabled for PostgreSQL-only setup
public interface RoomCompactedCodeStateRepository
extends MongoRepository<RoomCompactedCodeState, String> {
    public Optional<RoomCompactedCodeState> findByRoomIdAndProblemId(Long var1, Long var2);

    public List<RoomCompactedCodeState> findByRoomId(Long var1);
}
