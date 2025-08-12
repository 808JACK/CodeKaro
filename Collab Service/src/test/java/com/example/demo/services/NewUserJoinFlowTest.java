package com.example.demo.services;

import com.example.demo.dtos.OTOperationDTO;
import com.example.demo.entities.OTOperation;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomStatus;
import com.example.demo.entities.RoomType;
import com.example.demo.repos.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for the core "new user joins room" functionality
 * Uses SimpleOTService to test the logic without Cassandra complexity
 */
@ExtendWith(MockitoExtension.class)
class NewUserJoinFlowTest {

    @Mock
    private RoomRepository roomRepository;

    private SimpleOTService otService;
    private Room testRoom;
    private String roomCode;

    @BeforeEach
    void setUp() {
        otService = new SimpleOTService(roomRepository);
        
        roomCode = "TEST-ROOM-123";
        testRoom = Room.builder()
                .id(1L)
                .inviteCode(roomCode)
                .name("Test Room")
                .creatorUserId(1L)
                .roomType(RoomType.COLLAB)
                .status(RoomStatus.ACTIVE)
                .problemIds(new ArrayList<>())
                .build();

        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));
    }

    @Test
    void testNewUserJoinsRoom_ReceivesAllPreviousChanges() {
        // Given: User A makes some changes
        OTOperationDTO op1 = createOperation(1L, "insert", 0, "Hello ", 0);
        OTOperationDTO op2 = createOperation(1L, "insert", 6, "World", 1);
        OTOperationDTO op3 = createOperation(1L, "insert", 11, "!", 2);
        
        // Process operations
        otService.processOperation(roomCode, 1L, op1, 0);
        otService.processOperation(roomCode, 1L, op2, 1);
        otService.processOperation(roomCode, 1L, op3, 2);
        
        // When: New user (User B) joins and gets current state
        String initialCode = "// Write your solution here\n";
        String currentCode = otService.reconstructCurrentCode(roomCode, initialCode);
        
        // Then: User B should see all previous changes
        assertNotNull(currentCode);
        assertTrue(currentCode.contains("Hello World!"), 
                "Current code should contain all previous changes: " + currentCode);
        
        // Verify operation count
        assertEquals(3, otService.getOperationCount(roomCode));
    }

    private OTOperationDTO createOperation(Long userId, String type, int position, String chars, int version) {
        return OTOperationDTO.builder()
                .userId(userId)
                .roomCode(roomCode)
                .operationType(type)
                .position(position)
                .chars(chars)
                .version(version)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}