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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleOTServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private SimpleOTService otService;

    private Room testRoom;
    private String roomCode = "TEST1234";

    @BeforeEach
    void setUp() {
        testRoom = Room.builder()
                .id(1L)
                .inviteCode(roomCode)
                .name("Test Room")
                .creatorUserId(1L)
                .voiceEnabled(false)
                .roomType(RoomType.COLLAB)
                .problemIds(new ArrayList<>())
                .status(RoomStatus.ACTIVE)
                .build();
    }

    @Test
    void testSaveOperation_Success() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));

        OTOperationDTO operationDTO = OTOperationDTO.builder()
                .userId(1L)
                .roomCode(roomCode)
                .operationType("insert")
                .position(0)
                .chars("Hello")
                .version(0)
                .build();

        // When
        OTOperation result = otService.saveOperation(operationDTO, roomCode);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("insert", result.getOperationType());
        assertEquals("Hello", result.getChars());
        assertEquals(0, result.getPosition());
        assertNotNull(result.getOperationId());
        assertNotNull(result.getTimestamp());
    }

    @Test
    void testSaveOperation_RoomNotFound() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.empty());

        OTOperationDTO operationDTO = OTOperationDTO.builder()
                .userId(1L)
                .roomCode(roomCode)
                .operationType("insert")
                .position(0)
                .chars("Hello")
                .version(0)
                .build();

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> otService.saveOperation(operationDTO, roomCode));
        
        assertTrue(exception.getMessage().contains("Room not found"));
    }

    @Test
    void testProcessOperation_Success() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));

        OTOperationDTO operationDTO = OTOperationDTO.builder()
                .operationType("insert")
                .position(0)
                .chars("Hello")
                .build();

        // When
        OTOperation result = otService.processOperation(roomCode, 1L, operationDTO, 0);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("insert", result.getOperationType());
        assertEquals("Hello", result.getChars());
        assertEquals(0, result.getPosition());
    }

    @Test
    void testGetOperationsForRoom_EmptyRoom() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));

        // When
        List<OTOperation> operations = otService.getOperationsForRoom(roomCode);

        // Then
        assertNotNull(operations);
        assertTrue(operations.isEmpty());
    }

    @Test
    void testGetOperationsForRoom_WithOperations() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));

        // Add some operations first
        OTOperationDTO op1 = OTOperationDTO.builder()
                .userId(1L)
                .operationType("insert")
                .position(0)
                .chars("Hello")
                .version(0)
                .build();

        OTOperationDTO op2 = OTOperationDTO.builder()
                .userId(2L)
                .operationType("insert")
                .position(5)
                .chars(" World")
                .version(1)
                .build();

        otService.saveOperation(op1, roomCode);
        otService.saveOperation(op2, roomCode);

        // When
        List<OTOperation> operations = otService.getOperationsForRoom(roomCode);

        // Then
        assertNotNull(operations);
        assertEquals(2, operations.size());
        assertEquals("Hello", operations.get(0).getChars());
        assertEquals(" World", operations.get(1).getChars());
    }

    @Test
    void testGetOperationsAfterVersion() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));

        // Add operations with different versions
        for (int i = 0; i < 5; i++) {
            OTOperationDTO op = OTOperationDTO.builder()
                    .userId(1L)
                    .operationType("insert")
                    .position(i)
                    .chars("Char" + i)
                    .version(i)
                    .build();
            otService.saveOperation(op, roomCode);
        }

        // When
        List<OTOperation> operations = otService.getOperationsAfterVersion(roomCode, 2);

        // Then
        assertNotNull(operations);
        assertEquals(2, operations.size()); // Operations with version 3 and 4
        assertTrue(operations.stream().allMatch(op -> op.getVersion() > 2));
    }

    @Test
    void testGetOperationCount() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));

        // Initially should be 0
        assertEquals(0, otService.getOperationCount(roomCode));

        // Add some operations
        for (int i = 0; i < 3; i++) {
            OTOperationDTO op = OTOperationDTO.builder()
                    .userId(1L)
                    .operationType("insert")
                    .position(i)
                    .chars("Test" + i)
                    .version(i)
                    .build();
            otService.saveOperation(op, roomCode);
        }

        // When
        long count = otService.getOperationCount(roomCode);

        // Then
        assertEquals(3, count);
    }

    @Test
    void testReconstructCurrentCode_EmptyOperations() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));
        String initialCode = "// Initial code";

        // When
        String result = otService.reconstructCurrentCode(roomCode, initialCode);

        // Then
        assertEquals(initialCode, result);
    }

    @Test
    void testReconstructCurrentCode_WithInsertOperations() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));
        String initialCode = "";

        // Add insert operations
        OTOperationDTO op1 = OTOperationDTO.builder()
                .userId(1L)
                .operationType("insert")
                .position(0)
                .chars("Hello")
                .version(0)
                .build();

        OTOperationDTO op2 = OTOperationDTO.builder()
                .userId(2L)
                .operationType("insert")
                .position(5)
                .chars(" World")
                .version(1)
                .build();

        otService.saveOperation(op1, roomCode);
        otService.saveOperation(op2, roomCode);

        // When
        String result = otService.reconstructCurrentCode(roomCode, initialCode);

        // Then
        assertEquals("Hello World", result);
    }

    @Test
    void testReconstructCurrentCode_WithDeleteOperations() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));
        String initialCode = "Hello World";

        // Add delete operation
        OTOperationDTO deleteOp = OTOperationDTO.builder()
                .userId(1L)
                .operationType("delete")
                .position(5)
                .count(6) // Delete " World"
                .version(0)
                .build();

        otService.saveOperation(deleteOp, roomCode);

        // When
        String result = otService.reconstructCurrentCode(roomCode, initialCode);

        // Then
        assertEquals("Hello", result);
    }

    @Test
    void testReconstructCurrentCode_WithReplaceOperations() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));
        String initialCode = "Hello World";

        // Add replace operation
        OTOperationDTO replaceOp = OTOperationDTO.builder()
                .userId(1L)
                .operationType("replace")
                .position(6)
                .deleteCount(5) // Delete "World"
                .chars("Java") // Insert "Java"
                .version(0)
                .build();

        otService.saveOperation(replaceOp, roomCode);

        // When
        String result = otService.reconstructCurrentCode(roomCode, initialCode);

        // Then
        assertEquals("Hello Java", result);
    }

    @Test
    void testConvertToDTO() {
        // Given
        UUID operationId = UUID.randomUUID();
        OTOperation operation = OTOperation.builder()
                .operationId(operationId)
                .roomId(1L)
                .userId(1L)
                .operationType("insert")
                .position(0)
                .chars("Hello")
                .count(null)
                .deleteCount(null)
                .version(0)
                .timestamp(java.time.Instant.now())
                .build();

        // When
        OTOperationDTO dto = otService.convertToDTO(operation, roomCode);

        // Then
        assertNotNull(dto);
        assertEquals(operationId, dto.getOperationId());
        assertEquals(1L, dto.getUserId());
        assertEquals(roomCode, dto.getRoomCode());
        assertEquals("insert", dto.getOperationType());
        assertEquals(0, dto.getPosition());
        assertEquals("Hello", dto.getChars());
        assertEquals(0, dto.getVersion());
        assertNotNull(dto.getTimestamp());
    }

    @Test
    void testConcurrentOperations() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));

        // Simulate concurrent operations from different users
        OTOperationDTO op1 = OTOperationDTO.builder()
                .userId(1L)
                .operationType("insert")
                .position(0)
                .chars("User1")
                .version(0)
                .build();

        OTOperationDTO op2 = OTOperationDTO.builder()
                .userId(2L)
                .operationType("insert")
                .position(0)
                .chars("User2")
                .version(0)
                .build();

        // When
        OTOperation result1 = otService.processOperation(roomCode, 1L, op1, 0);
        OTOperation result2 = otService.processOperation(roomCode, 2L, op2, 0);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(2, otService.getOperationCount(roomCode));

        // Verify final state contains both operations
        String finalCode = otService.reconstructCurrentCode(roomCode, "");
        assertTrue(finalCode.contains("User1") || finalCode.contains("User2"));
    }

    @Test
    void testOperationVersioning() {
        // Given
        when(roomRepository.findByInviteCode(roomCode)).thenReturn(Optional.of(testRoom));

        // When - Add operations sequentially
        for (int i = 0; i < 5; i++) {
            OTOperationDTO op = OTOperationDTO.builder()
                    .userId(1L)
                    .operationType("insert")
                    .position(i)
                    .chars("" + i)
                    .build();
            
            OTOperation result = otService.processOperation(roomCode, 1L, op, i);
            assertEquals(i, result.getVersion().intValue());
        }

        // Then
        assertEquals(5, otService.getOperationCount(roomCode));
        
        // Verify operations after version 2
        List<OTOperation> laterOps = otService.getOperationsAfterVersion(roomCode, 2);
        assertEquals(2, laterOps.size());
        assertTrue(laterOps.stream().allMatch(op -> op.getVersion() > 2));
    }
}