/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestTemplate
 */
package com.example.demo.services;

import com.example.demo.dtos.CreateRoomRequest;
import com.example.demo.dtos.ParticipantDto;
import com.example.demo.dtos.ProblemDetailsDto;
import com.example.demo.dtos.ProblemResponse;
import com.example.demo.dtos.RoomCreatedResponse;
import com.example.demo.dtos.RoomDetailsResponse;
import com.example.demo.entities.InviteCodeGenerator;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomStatus;
import com.example.demo.entities.RoomType;
import com.example.demo.exceptions.RoomNotFoundException;
import com.example.demo.repos.RoomRepository;
import com.example.demo.services.OTServiceInterface;
import com.example.demo.services.SessionRegistryService;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RoomService {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(RoomService.class);
    private final RoomRepository roomRepository;
    private final SessionRegistryService sessionRegistryService;
    private final RestTemplate restTemplate;
    private final OTServiceInterface otService;
    private static final String PROBLEM_SERVICE_URL = "http://localhost:8082/problem";

    public RoomService(RoomRepository roomRepository, SessionRegistryService sessionRegistryService, RestTemplate restTemplate, OTServiceInterface otService) {
        this.roomRepository = roomRepository;
        this.sessionRegistryService = sessionRegistryService;
        this.restTemplate = restTemplate;
        this.otService = otService;
    }

    public RoomCreatedResponse createRoom(CreateRoomRequest request, Long creatorUserId) {
        try {
            String inviteCode = InviteCodeGenerator.generateInviteCode();
            Room room = Room.builder().inviteCode(inviteCode).name(request.getName()).creatorUserId(creatorUserId).voiceEnabled(request.isVoiceEnabled()).roomType(RoomType.valueOf(request.getRoomType().toUpperCase())).problemIds(new ArrayList<Long>(request.getProblemIds())).status(RoomStatus.ACTIVE).build();
            Room savedRoom = (Room)this.roomRepository.save(room);
            log.info("Created room {} with invite code {} for user {}", new Object[]{savedRoom.getId(), inviteCode, creatorUserId});
            return RoomCreatedResponse.builder().roomId(savedRoom.getId()).inviteCode(inviteCode).build();
        }
        catch (Exception e) {
            log.error("Error creating room for user {}: {}", new Object[]{creatorUserId, e.getMessage(), e});
            throw new RuntimeException("Failed to create room", e);
        }
    }

    public RoomDetailsResponse getRoomDetails(String inviteCode, Long userId) {
        try {
            Room room = this.roomRepository.findByInviteCode(inviteCode).orElseThrow(() -> new RoomNotFoundException("Room not found with code: " + inviteCode));
            if (room.getStatus() != RoomStatus.ACTIVE) {
                throw new RoomNotFoundException("Room is not active");
            }
            List<ParticipantDto> participants = this.sessionRegistryService.getActiveParticipants(inviteCode);
            List<ProblemDetailsDto> problems = this.getProblemsForRoom(room);
            String initialCode = this.getInitialCodeForRoom(room);
            String currentCode = this.otService.reconstructCurrentCode(inviteCode, initialCode);
            return RoomDetailsResponse.builder().roomCode(inviteCode).roomName(room.getName()).participants(participants).participantCount(participants.size()).currentCode(currentCode).language("java").timestamp(System.currentTimeMillis()).voiceEnabled(room.getVoiceEnabled()).problems(problems).build();
        }
        catch (RoomNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("Error getting room details for code {}: {}", new Object[]{inviteCode, e.getMessage(), e});
            throw new RuntimeException("Failed to get room details", e);
        }
    }

    public boolean isValidRoom(String inviteCode) {
        return this.roomRepository.findByInviteCode(inviteCode).map(room -> room.getStatus() == RoomStatus.ACTIVE).orElse(false);
    }

    private String getInitialCodeForRoom(Room room) {
        if (room == null || room.getProblemIds() == null || room.getProblemIds().isEmpty()) {
            return "// Write your solution here\n";
        }
        try {
            Long problemId = room.getProblemIds().get(0);
            String url = "http://localhost:8082/problem/" + problemId + "?language=java";
            ProblemResponse response = (ProblemResponse)this.restTemplate.getForObject(url, ProblemResponse.class, new Object[0]);
            if (response != null && response.getTemplate() != null) {
                return response.getTemplate();
            }
        }
        catch (Exception e) {
            log.warn("Failed to fetch template for problem {}: {}", (Object)room.getProblemIds().get(0), (Object)e.getMessage());
        }
        return "// Write your solution here\n";
    }

    private List<ProblemResponse> fetchProblemsForRoom(Room room) {
        ArrayList<ProblemResponse> problems = new ArrayList<ProblemResponse>();
        if (room == null || room.getProblemIds() == null || room.getProblemIds().isEmpty()) {
            return problems;
        }
        for (Long problemId : room.getProblemIds()) {
            try {
                String url = "http://localhost:8082/problem/" + problemId + "?language=java";
                ProblemResponse response = (ProblemResponse)this.restTemplate.getForObject(url, ProblemResponse.class, new Object[0]);
                if (response == null) continue;
                problems.add(response);
            }
            catch (Exception e) {
                log.warn("Failed to fetch problem {}: {}", (Object)problemId, (Object)e.getMessage());
            }
        }
        return problems;
    }

    public List<ProblemDetailsDto> getProblemsForRoom(Room room) {
        ArrayList<ProblemDetailsDto> problems = new ArrayList<ProblemDetailsDto>();
        if (room == null || room.getProblemIds() == null || room.getProblemIds().isEmpty()) {
            return problems;
        }
        try {
            for (Long problemId : room.getProblemIds()) {
                try {
                    String url = "http://localhost:8082/problem/" + problemId + "?language=java";
                    ProblemResponse response = (ProblemResponse)this.restTemplate.getForObject(url, ProblemResponse.class, new Object[0]);
                    if (response == null) continue;
                    ProblemDetailsDto problemDto = ProblemDetailsDto.builder().id(response.getId()).title(response.getTitle()).description(response.getContent()).difficulty(response.getDifficulty()).templateCode(response.getTemplate()).language("java").build();
                    problems.add(problemDto);
                }
                catch (Exception e) {
                    log.warn("Failed to fetch problem {}: {}", (Object)problemId, (Object)e.getMessage());
                }
            }
        }
        catch (Exception e) {
            log.error("Error fetching problems for room: {}", (Object)e.getMessage(), (Object)e);
        }
        return problems;
    }
}
