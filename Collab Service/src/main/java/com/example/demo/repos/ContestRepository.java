/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.stereotype.Repository
 */
package com.example.demo.repos;

import com.example.demo.entities.Contest;
import com.example.demo.entities.ContestStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository
extends JpaRepository<Contest, Long> {
    public Optional<Contest> findByInviteLink(String var1);

    public boolean existsByInviteLink(String var1);
    
    public List<Contest> findByStatus(ContestStatus status);
}
