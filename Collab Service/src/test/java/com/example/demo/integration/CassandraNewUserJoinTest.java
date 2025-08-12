package com.example.demo.integration;

import com.example.demo.dtos.*;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomStatus;
import com.example.demo.entities.RoomType;
import com.example.demo.repos.RoomRepository;
import com.example.demo.services.OTServiceInterface;
import com.example.demo.services.SessionRegistryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for new user join functionality
 * Tests the complete flow when a new user joins a room with existing operations
 * Uses SimpleOTService for reliable testing (same logic applies to Cassandra)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "cassandra.enabled=false",  // Use SimpleOTService for integration tests (same logic)
    "mongodb.enabled=false",
    "logging.level.com.example.demo=DEBUG",
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CassandraNewUserJoinTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SessionRegistryService sessionRegistryService;

    @Autowired
    private OTServiceInterface otService; // Will be SimpleOTService due to cassandra.enabled=false

    private WebSocketStompClient stompClient;
    private ObjectMapper objectMapper;
    private String roomCode;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        // Clean up any existing test rooms
        roomRepository.deleteAll();
        
        // Set up WebSocket client
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(new ObjectMapper());
        stompClient.setMessageConverter(messageConverter);
        stompClient.setDefaultHeartbeat(new long[]{0, 0});
        objectMapper = new ObjectMapper();

        // Create test room
        String uniqueCode = "TEST" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        testRoom = Room.builder()
                .inviteCode(uniqueCode)
                .name("Cassandra Test Room")
                .creatorUserId(1L)
                .voiceEnabled(false)
                .roomType(RoomType.COLLAB)
                .problemIds(new ArrayList<>())
                .status(RoomStatus.ACTIVE)
                .build();
        
        testRoom = roomRepository.save(testRoom);
        roomCode = testRoom.getInviteCode();
    }

    @AfterEach
    void tearDown() {
        try {
            if (testRoom != null && testRoom.getId() != null) {
                roomRepository.deleteById(testRoom.getId());
            }
            sessionRegistryService.clearAllSessions();
        } catch (Exception e) {
            // Ignore cleanup errors in tests
        }
    }

    @Test
    void testNewUserJoinsRoomWithExistingOperations() throws Exception {
        // Given: First user joins and makes some changes
        BlockingQueue<OTOperationDTO> user1Operations = new LinkedBlockingQueue<>();
        BlockingQueue<CodeStateDTO> user1CodeStates = new LinkedBlockingQueue<>();
        
        StompSession user1Session = connectUser(1L, user1Operations, user1CodeStates);
        joinRoom(user1Session, 1L);
        
        // User 1 makes several operations
        sendOperation(user1Session, 1L, "insert", 0, "Hello ", 0);
        Thread.sleep(100);
        sendOperation(user1Session, 1L, "insert", 6, "World", 1);
        Thread.sleep(100);
        sendOperation(user1Session, 1L, "insert", 11, "!", 2);
        Thread.sleep(500); // Allow operations to be processed

        // When: New user (User 2) joins the room
        BlockingQueue<OTOperationDTO> user2Operations = new LinkedBlockingQueue<>();
        BlockingQueue<CodeStateDTO> user2CodeStates = new LinkedBlockingQueue<>();
        BlockingQueue<ParticipantEventDTO> user2Events = new LinkedBlockingQueue<>();
        
        StompSession user2Session = connectUserWithCodeState(2L, user2Operations, user2CodeStates, user2Events);
        joinRoom(user2Session, 2L);

        // Then: User 2 should receive current code state with all previous changes
        CodeStateDTO codeState = user2CodeStates.poll(5, TimeUnit.SECONDS);
        assertNotNull(codeState, "New user should receive current code state");
        
        String currentCode = codeState.getCode();
        assertNotNull(currentCode);
        assertTrue(currentCode.contains("Hello World!"), 
                "Current code should contain all previous changes: " + currentCode);
        
        // Verify version is correct (should be 3 after 3 operations)
        assertTrue(codeState.getVersion() >= 3, 
                "Version should reflect all previous operations: " + codeState.getVersion());

        // When: User 2 makes a new operation
        sendOperation(user2Session, 2L, "insert", currentCode.length(), " From User2", codeState.getVersion());
        
        // Then: User 1 should receive User 2's operation
        OTOperationDTO receivedOp = user1Operations.poll(3, TimeUnit.SECONDS);
        assertNotNull(receivedOp, "User 1 should receive User 2's operation");
        assertEquals(2L, receivedOp.getUserId());
        assertEquals(" From User2", receivedOp.getChars());

        user1Session.disconnect();
        user2Session.disconnect();
    }

    @Test
    void testMultipleNewUsersJoinSequentially() throws Exception {
        // Given: User 1 creates some content
        StompSession user1Session = connectUser(1L, new LinkedBlockingQueue<>(), new LinkedBlockingQueue<>());
        joinRoom(user1Session, 1L);
        
        sendOperation(user1Session, 1L, "insert", 0, "Line 1\n", 0);
        sendOperation(user1Session, 1L, "insert", 7, "Line 2\n", 1);
        Thread.sleep(500);

        // When: User 2 joins
        BlockingQueue<CodeStateDTO> user2CodeStates = new LinkedBlockingQueue<>();
        StompSession user2Session = connectUserWithCodeState(2L, new LinkedBlockingQueue<>(), user2CodeStates, new LinkedBlockingQueue<>());
        joinRoom(user2Session, 2L);
        
        CodeStateDTO user2State = user2CodeStates.poll(3, TimeUnit.SECONDS);
        assertNotNull(user2State);
        assertTrue(user2State.getCode().contains("Line 1"));
        assertTrue(user2State.getCode().contains("Line 2"));

        // User 2 adds content
        sendOperation(user2Session, 2L, "insert", user2State.getCode().length(), "Line 3\n", user2State.getVersion());
        Thread.sleep(300);

        // When: User 3 joins
        BlockingQueue<CodeStateDTO> user3CodeStates = new LinkedBlockingQueue<>();
        StompSession user3Session = connectUserWithCodeState(3L, new LinkedBlockingQueue<>(), user3CodeStates, new LinkedBlockingQueue<>());
        joinRoom(user3Session, 3L);
        
        CodeStateDTO user3State = user3CodeStates.poll(3, TimeUnit.SECONDS);
        assertNotNull(user3State);
        
        // Then: User 3 should see all previous changes from both users
        String finalCode = user3State.getCode();
        assertTrue(finalCode.contains("Line 1"), "Should contain User 1's first change");
        assertTrue(finalCode.contains("Line 2"), "Should contain User 1's second change");
        assertTrue(finalCode.contains("Line 3"), "Should contain User 2's change");
        assertTrue(user3State.getVersion() >= 3, "Version should reflect all operations");

        user1Session.disconnect();
        user2Session.disconnect();
        user3Session.disconnect();
    }

    @Test
    void testNewUserJoinsEmptyRoom() throws Exception {
        // When: User joins empty room (no previous operations)
        BlockingQueue<CodeStateDTO> codeStates = new LinkedBlockingQueue<>();
        StompSession userSession = connectUserWithCodeState(1L, new LinkedBlockingQueue<>(), codeStates, new LinkedBlockingQueue<>());
        joinRoom(userSession, 1L);

        // Then: Should receive initial template code
        CodeStateDTO codeState = codeStates.poll(3, TimeUnit.SECONDS);
        assertNotNull(codeState, "Should receive initial code state");
        assertEquals("// Write your solution here\n", codeState.getCode());
        assertEquals(0, codeState.getVersion());

        userSession.disconnect();
    }

    // Helper methods

    private StompSession connectUser(Long userId, BlockingQueue<OTOperationDTO> operationQueue, 
                                   BlockingQueue<CodeStateDTO> codeStateQueue) throws Exception {
        return connectUserWithCodeState(userId, operationQueue, codeStateQueue, new LinkedBlockingQueue<>());
    }

    private StompSession connectUserWithCodeState(Long userId, 
                                                BlockingQueue<OTOperationDTO> operationQueue,
                                                BlockingQueue<CodeStateDTO> codeStateQueue,
                                                BlockingQueue<ParticipantEventDTO> eventQueue) throws Exception {
        final Long currentUserId = userId;
        StompSession session = stompClient.connectAsync("ws://localhost:" + port + "/collab/ws", new StompSessionHandlerAdapter() {}).get();

        // Subscribe to OT operations
        session.subscribe("/collab-topic/room/" + roomCode + "/ot-operations", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return java.util.Map.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    if (payload instanceof java.util.Map) {
                        java.util.Map<String, Object> map = (java.util.Map<String, Object>) payload;
                        Object operationObj = map.get("operation");
                        Object senderIdObj = map.get("senderId");
                        
                        if (operationObj != null && senderIdObj != null) {
                            String json = objectMapper.writeValueAsString(operationObj);
                            OTOperationDTO operation = objectMapper.readValue(json, OTOperationDTO.class);
                            Long senderId = ((Number) senderIdObj).longValue();
                            
                            // Only add to queue if it's not from the current user
                            if (!senderId.equals(currentUserId)) {
                                operationQueue.offer(operation);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Subscribe to code state updates
        session.subscribe("/collab-topic/room/" + roomCode + "/code-state", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CodeStateDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                if (payload instanceof CodeStateDTO) {
                    codeStateQueue.offer((CodeStateDTO) payload);
                }
            }
        });

        // Subscribe to participant events
        session.subscribe("/collab-topic/room/" + roomCode + "/participants", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ParticipantEventDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                eventQueue.offer((ParticipantEventDTO) payload);
            }
        });

        return session;
    }

    private void joinRoom(StompSession session, Long userId) throws Exception {
        JoinRoomRequestDTO joinRequest = new JoinRoomRequestDTO();
        joinRequest.setInviteCode(roomCode);
        joinRequest.setUserId(userId);
        session.send("/collab-app/room/join", joinRequest);
        Thread.sleep(500); // Allow join to be processed
    }

    private void sendOperation(StompSession session, Long userId, String type, int position, String chars, int version) throws Exception {
        OTOperationDTO operation = OTOperationDTO.builder()
                .userId(userId)
                .roomCode(roomCode)
                .operationType(type)
                .position(position)
                .chars(chars)
                .version(version)
                .timestamp(System.currentTimeMillis())
                .build();

        session.send("/collab-app/ot/operation", operation);
    }
}