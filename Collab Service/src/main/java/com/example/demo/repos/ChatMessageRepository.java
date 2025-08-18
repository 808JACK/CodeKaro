/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.mongodb.repository.MongoRepository
 *  org.springframework.stereotype.Repository
 */
package com.example.demo.repos;

import com.example.demo.entities.ChatMessage;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// @Repository - Disabled for PostgreSQL-only setup
public interface ChatMessageRepository
extends MongoRepository<ChatMessage, String> {
    public List<ChatMessage> findByRoomId(Long var1);
}
