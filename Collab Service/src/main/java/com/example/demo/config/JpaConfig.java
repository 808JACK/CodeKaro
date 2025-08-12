/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.autoconfigure.domain.EntityScan
 *  org.springframework.context.annotation.ComponentScan$Filter
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.context.annotation.FilterType
 *  org.springframework.data.jpa.repository.config.EnableJpaRepositories
 */
package com.example.demo.config;

import com.example.demo.entities.RoomCodeState;
import com.example.demo.entities.RoomParticipants;
import com.example.demo.entities.RoomType;
import com.example.demo.repos.ChatMessageRepository;
import com.example.demo.repos.ContestSubmissionRepository;
import com.example.demo.repos.OTOperationRepository;
import com.example.demo.repos.RoomCompactedCodeStateRepository;
import com.example.demo.repos.UserRecentRoomsRepository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages={"com.example.demo.repos"}, excludeFilters={@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value={RoomCompactedCodeStateRepository.class, UserRecentRoomsRepository.class, ChatMessageRepository.class, ContestSubmissionRepository.class, OTOperationRepository.class})})
@EntityScan(basePackageClasses={RoomCodeState.class, RoomParticipants.class, RoomType.class})
public class JpaConfig {
}
