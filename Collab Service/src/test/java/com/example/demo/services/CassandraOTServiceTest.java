package com.example.demo.services;

import com.example.demo.dtos.OTOperationDTO;
import com.example.demo.entities.OTOperation;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomStatus;
import com.example.demo.entities.RoomType;
import com.example.demo.repos.OTOperationRepository;
import com.example.demo.repos.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for Cassandra-based OTService
 * Tests the scenario where new users join rooms and need to receive all previous changes
 */
@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "cassandra.enabled=true"
})
public class CassandraOTServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private OTOperationRepository otOperationRepository;

    private OTService otService;
    private Room testRoom;
    private String roomCode;

    @BeforeEach
    void setUp() {
        otService = new OTService(roomRepository, otOperationRepository);
        
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
        // Given: Room has existing operations from previous users
        List<OTOperation> existingOperations = createExistingOperations();
        when(otOperationRepository.findByRoomIdOrderByOperationId(testRoom.getId()))
                .thenReturn(existingOperations);
        when(otOperationRepository.save(any(OTOperation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When: New user joins and requests current code state
        String initialCode = "// Write your solution here\n";
        String currentCode = otService.reconstructCurrentCode(roomCode, initialCode);

        // Then: Current code should reflect all previous changes
        assertNotNull(currentCode);
        assertTrue(currentCode.contains("Hello"));
        assertTrue(currentCode.contains("World"));
        assertTrue(currentCode.contains("Java"));
        
        // Verify the final reconstructed code
        String expectedCode = "// Write your solution here\nHello World Java";
        assertEquals(expectedCode, currentCode);
        
        verify(otOperationRepository).findByRoomIdOrderByOperationId(testRoom.getId());
    }

    @Test
    void testNewUserJoinsRoom_GetsCorrectOperationCount() {
        // Given: Room has 5 existing operations
        when(otOperationRepository.countByRoomId(testRoom.getId())).thenReturn(5L);

        // When: New user requests operation count
        long operationCount = otService.getOperationCount(roomCode);

        // Then: Should return correct count
        assertEquals(5L, operationCount);
        verify(otOperationRepository).countByRoomId(testRoom.getId());
    }

    @Test
    void testNewUserJoinsRoom_CanSyncFromSpecificVersion() {
        // Given: Room has operations and user wants to sync from version 2
        List<OTOperation> operationsAfterVersion2 = createOperationsAfterVersion(2);
        when(otOperationRepository.findByRoomIdAndVersionGreaterThan(testRoom.getId(), 2))
                .thenReturn(operationsAfterVersion2);

        // When: New user syncs from version 2
        List<OTOperation> syncOperations = otService.getOperationsAfterVersion(roomCode, 2);

        // Then: Should get only operations after version 2
        assertEquals(2, syncOperations.size());
        assertTrue(syncOperations.stream().allMatch(op -> op.getVersion() > 2));
        verify(otOperationRepository).findByRoomIdAndVersionGreaterThan(testRoom.getId(), 2);
    }

    @Test
    void testConcurrentUsersAndNewUserJoins() {
        // Given: Multiple users have been editing concurrently
        List<OTOperation> concurrentOperations = createConcurrentOperations();
        when(otOperationRepository.findByRoomIdOrderByOperationId(testRoom.getId()))
                .thenReturn(concurrentOperations);

        // When: New user joins and gets current state
        String initialCode = "";
        String currentCode = otService.reconstructCurrentCode(roomCode, initialCode);

        // Then: Should get properly merged state from all concurrent operations
        assertNotNull(currentCode);
        assertTrue(currentCode.contains("User1"));
        assertTrue(currentCode.contains("User2"));
        assertTrue(currentCode.contains("User3"));
        
        verify(otOperationRepository).findByRoomIdOrderByOperationId(testRoom.getId());
    }

    @Test
    void testNewUserJoinsEmptyRoom() {
        // Given: Room has no operations
        when(otOperationRepository.findByRoomIdOrderByOperationId(testRoom.getId()))
                .thenReturn(new ArrayList<>());

        // When: New user joins empty room
        String initialCode = "// Initial template\n";
        String currentCode = otService.reconstructCurrentCode(roomCode, initialCode);

        // Then: Should get initial template code
        assertEquals(initialCode, currentCode);
        verify(otOperationRepository).findByRoomIdOrderByOperationId(testRoom.getId());
    }

    @Test
    void testNewUserJoinsRoom_ProcessesNewOperation() {
        // Given: Room has existing operations
        when(otOperationRepository.countByRoomId(testRoom.getId())).thenReturn(3L);
        when(otOperationRepository.findByRoomIdOrderByOperationId(testRoom.getId()))
                .thenReturn(createExistingOperations());

        OTOperationDTO newOperation = OTOperationDTO.builder()
                .userId(999L)
                .roomCode(roomCode)
                .operationType("insert")
                .position(0)
                .chars("NewUser: ")
                .version(0) // Client thinks they're at version 0
                .build();

        OTOperation savedOperation = OTOperation.builder()
                .operationId(UUID.randomUUID())
                .roomId(testRoom.getId())
                .userId(999L)
                .operationType("insert")
                .position(0)
                .chars("NewUser: ")
                .version(3) // Server assigns version 3
                .timestamp(Instant.now())
                .build();

        when(otOperationRepository.save(any(OTOperation.class))).thenReturn(savedOperation);

        // When: New user sends their first operation
        OTOperation result = otService.processOperation(roomCode, 999L, newOperation, 0);

        // Then: Operation should be processed and saved with correct server version
        assertNotNull(result);
        assertEquals(999L, result.getUserId());
        assertEquals(3, result.getVersion()); // Server version, not client version
        
        verify(otOperationRepository).countByRoomId(testRoom.getId());
        verify(otOperationRepository).save(any(OTOperation.class));
    }

    @Test
    void testCompleteNewUserJoinFlow() {
        // Given: Simulate a complete flow of new user joining active room
        List<OTOperation> existingOps = createComplexEditingHistory();
        when(otOperationRepository.findByRoomIdOrderByOperationId(testRoom.getId()))
                .thenReturn(existingOps);
        when(otOperationRepository.countByRoomId(testRoom.getId()))
                .thenReturn((long) existingOps.size());

        // When: New user joins room
        // 1. Get current operation count (for version)
        long currentVersion = otService.getOperationCount(roomCode);
        
        // 2. Reconstruct current code state
        String initialCode = "public class Solution {\n}\n";
        String currentCode = otService.reconstructCurrentCode(roomCode, initialCode);
        
        // 3. Get all operations for potential sync
        List<OTOperation> allOperations = otService.getOperationsForRoom(roomCode);

        // Then: New user should have complete picture of room state
        assertEquals(6L, currentVersion);
        assertNotNull(currentCode);
        assertTrue(currentCode.contains("method1"));
        assertTrue(currentCode.contains("method2"));
        assertEquals(6, allOperations.size());
        
        // Verify all repository calls
        verify(otOperationRepository).countByRoomId(testRoom.getId());
        verify(otOperationRepository, times(2)).findByRoomIdOrderByOperationId(testRoom.getId());
    }

    // Helper methods to create test data

    private List<OTOperation> createExistingOperations() {
        return Arrays.asList(
            createOperation(1L, "insert", 29, "Hello ", 0),
            createOperation(2L, "insert", 35, "World ", 1),
            createOperation(3L, "insert", 41, "Java", 2)
        );
    }

    private List<OTOperation> createOperationsAfterVersion(int version) {
        return Arrays.asList(
            createOperation(4L, "insert", 45, "!", 3),
            createOperation(5L, "insert", 46, "\n", 4)
        );
    }

    private List<OTOperation> createConcurrentOperations() {
        return Arrays.asList(
            createOperation(1L, "insert", 0, "User1: Hello\n", 0),
            createOperation(2L, "insert", 14, "User2: World\n", 1),
            createOperation(3L, "insert", 28, "User3: Java\n", 2)
        );
    }

    private List<OTOperation> createComplexEditingHistory() {
        return Arrays.asList(
            createOperation(1L, "insert", 20, "public void method1() {\n", 0),
            createOperation(2L, "insert", 45, "    System.out.println(\"Hello\");\n", 1),
            createOperation(1L, "insert", 79, "}\n", 2),
            createOperation(3L, "insert", 81, "public void method2() {\n", 3),
            createOperation(3L, "insert", 106, "    return 42;\n", 4),
            createOperation(2L, "insert", 120, "}\n", 5)
        );
    }

    private OTOperation createOperation(Long userId, String type, int position, String chars, int version) {
        return OTOperation.builder()
                .operationId(UUID.randomUUID())
                .roomId(testRoom.getId())
                .userId(userId)
                .operationType(type)
                .position(position)
                .chars(chars)
                .version(version)
                .timestamp(Instant.now())
                .build();
    }
}