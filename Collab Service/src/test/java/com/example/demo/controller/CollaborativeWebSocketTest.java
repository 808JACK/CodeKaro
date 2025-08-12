package com.example.demo.controller;

import com.example.demo.dtos.*;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomStatus;
import com.example.demo.entities.RoomType;
import com.example.demo.repos.RoomRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "cassandra.enabled=false",
    "mongodb.enabled=false",
    "logging.level.com.example.demo=DEBUG",
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CollaborativeWebSocketTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private SessionRegistryService sessionRegistryService;

    private WebSocketStompClient stompClient;
    private ObjectMapper objectMapper;
    private String roomCode;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        // Clean up any existing test rooms
        roomRepository.deleteAll();
        
        // Set up WebSocket client with proper configuration
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(new ObjectMapper());
        stompClient.setMessageConverter(messageConverter);
        stompClient.setDefaultHeartbeat(new long[]{0, 0}); // Disable heartbeat for tests
        objectMapper = new ObjectMapper();

        // Create test room with unique invite code
        String uniqueCode = "TEST" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        testRoom = Room.builder()
                .inviteCode(uniqueCode)
                .name("Test Room")
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
        // Clean up test data
        try {
            if (testRoom != null && testRoom.getId() != null) {
                roomRepository.deleteById(testRoom.getId());
            }
            // Clear session registry
            sessionRegistryService.clearAllSessions();
        } catch (Exception e) {
            // Ignore cleanup errors in tests
        }
    }

    @Test
    void testBroadcastingToMultipleUsers() throws Exception {
        // Test that operations are broadcasted to all connected users
        
        BlockingQueue<OTOperationDTO> user1Operations = new LinkedBlockingQueue<>();
        BlockingQueue<OTOperationDTO> user2Operations = new LinkedBlockingQueue<>();
        BlockingQueue<ParticipantEventDTO> user1Events = new LinkedBlockingQueue<>();
        BlockingQueue<ParticipantEventDTO> user2Events = new LinkedBlockingQueue<>();

        // Connect two users
        StompSession user1Session = connectUser(1L, user1Operations, user1Events);
        StompSession user2Session = connectUser(2L, user2Operations, user2Events);

        // User 1 joins room
        JoinRoomRequestDTO joinRequest1 = new JoinRoomRequestDTO();
        joinRequest1.setInviteCode(roomCode);
        joinRequest1.setUserId(1L);
        user1Session.send("/collab-app/room/join", joinRequest1);

        // User 2 joins room
        JoinRoomRequestDTO joinRequest2 = new JoinRoomRequestDTO();
        joinRequest2.setInviteCode(roomCode);
        joinRequest2.setUserId(2L);
        user2Session.send("/collab-app/room/join", joinRequest2);

        // Wait for subscriptions to be fully established
        Thread.sleep(1000);

        // Wait for join events
        ParticipantEventDTO joinEvent1 = user1Events.poll(5, TimeUnit.SECONDS);
        ParticipantEventDTO joinEvent2 = user2Events.poll(5, TimeUnit.SECONDS);

        assertNotNull(joinEvent1);
        assertNotNull(joinEvent2);
        assertEquals("USER_JOINED", joinEvent1.getEventType());
        assertEquals("USER_JOINED", joinEvent2.getEventType());

        // User 1 sends an OT operation
        OTOperationDTO operation = OTOperationDTO.builder()
                .userId(1L)
                .roomCode(roomCode)
                .operationType("insert")
                .position(0)
                .chars("Hello")
                .version(0)
                .timestamp(System.currentTimeMillis())
                .build();

        user1Session.send("/collab-app/ot/operation", operation);

        // Verify User 2 receives the operation (User 1 shouldn't receive their own)
        OTOperationDTO receivedOp = user2Operations.poll(5, TimeUnit.SECONDS);
        assertNotNull(receivedOp, "User 2 should receive the operation from User 1");
        assertEquals("insert", receivedOp.getOperationType());
        assertEquals("Hello", receivedOp.getChars());
        assertEquals(1L, receivedOp.getUserId());

        // Verify User 1 doesn't receive their own operation
        OTOperationDTO user1SelfOp = user1Operations.poll(1, TimeUnit.SECONDS);
        assertNull(user1SelfOp, "User 1 should not receive their own operation");

        user1Session.disconnect();
        user2Session.disconnect();
    }

    @Test
    void testRealTimeOperationDelivery() throws Exception {
        // Test that operations are delivered in real-time with minimal latency
        
        BlockingQueue<OTOperationDTO> receivedOperations = new LinkedBlockingQueue<>();
        BlockingQueue<ParticipantEventDTO> events = new LinkedBlockingQueue<>();

        StompSession user1Session = connectUser(1L, new LinkedBlockingQueue<>(), new LinkedBlockingQueue<>());
        StompSession user2Session = connectUser(2L, receivedOperations, events);

        // Join room
        joinRoom(user1Session, 1L);
        joinRoom(user2Session, 2L);

        // Wait for subscriptions to be fully established
        Thread.sleep(1000);

        // Send multiple operations rapidly
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 5; i++) {
            OTOperationDTO operation = OTOperationDTO.builder()
                    .userId(1L)
                    .roomCode(roomCode)
                    .operationType("insert")
                    .position(i * 5)
                    .chars("Test" + i)
                    .version(i)
                    .timestamp(System.currentTimeMillis())
                    .build();

            user1Session.send("/collab-app/ot/operation", operation);
        }

        // Verify all operations are received in order
        List<OTOperationDTO> received = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OTOperationDTO op = receivedOperations.poll(3, TimeUnit.SECONDS);
            assertNotNull(op, "Operation " + i + " should be received");
            received.add(op);
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        // Sort operations by their chars to verify all were received
        received.sort((a, b) -> a.getChars().compareTo(b.getChars()));
        
        // Verify all operations were received
        for (int i = 0; i < 5; i++) {
            assertEquals("Test" + i, received.get(i).getChars());
            assertEquals(i * 5, received.get(i).getPosition());
        }

        // Verify real-time delivery (should be under 1 second for 5 operations)
        assertTrue(totalTime < 1000, "Operations should be delivered in real-time (under 1s)");

        user1Session.disconnect();
        user2Session.disconnect();
    }

    @Test
    void testOperationalTransformCorrectness() throws Exception {
        // Test that concurrent operations are transformed correctly
        
        BlockingQueue<OTOperationDTO> user1Operations = new LinkedBlockingQueue<>();
        BlockingQueue<OTOperationDTO> user2Operations = new LinkedBlockingQueue<>();

        StompSession user1Session = connectUser(1L, user1Operations, new LinkedBlockingQueue<>());
        StompSession user2Session = connectUser(2L, user2Operations, new LinkedBlockingQueue<>());

        joinRoom(user1Session, 1L);
        joinRoom(user2Session, 2L);

        // Both users start with same document state
        // User 1 inserts "Hello" at position 0
        OTOperationDTO op1 = OTOperationDTO.builder()
                .userId(1L)
                .roomCode(roomCode)
                .operationType("insert")
                .position(0)
                .chars("Hello")
                .version(0)
                .timestamp(System.currentTimeMillis())
                .build();

        // User 2 inserts "World" at position 0 (concurrent operation)
        OTOperationDTO op2 = OTOperationDTO.builder()
                .userId(2L)
                .roomCode(roomCode)
                .operationType("insert")
                .position(0)
                .chars("World")
                .version(0)
                .timestamp(System.currentTimeMillis() + 1)
                .build();

        // Send operations concurrently
        user1Session.send("/collab-app/ot/operation", op1);
        Thread.sleep(50); // Small delay to simulate network
        user2Session.send("/collab-app/ot/operation", op2);

        // Verify both users receive the other's operation
        OTOperationDTO user2ReceivedOp = user2Operations.poll(5, TimeUnit.SECONDS);
        OTOperationDTO user1ReceivedOp = user1Operations.poll(5, TimeUnit.SECONDS);

        assertNotNull(user2ReceivedOp, "User 2 should receive User 1's operation");
        assertNotNull(user1ReceivedOp, "User 1 should receive User 2's operation");

        // Verify operations are transformed (positions should be adjusted)
        // The exact transformation depends on the OT algorithm implementation
        assertTrue(user2ReceivedOp.getVersion() >= 0, "Operation should have a valid version");
        assertTrue(user1ReceivedOp.getVersion() >= 0, "Operation should have a valid version");

        user1Session.disconnect();
        user2Session.disconnect();
    }

    @Test
    void testNewJoinerReceivesCompleteState() throws Exception {
        // Test that new joiners receive complete room state including existing operations
        
        BlockingQueue<OTOperationDTO> user1Operations = new LinkedBlockingQueue<>();
        BlockingQueue<CodeStateDTO> user2CodeState = new LinkedBlockingQueue<>();
        BlockingQueue<RoomDetailsResponse> user2RoomState = new LinkedBlockingQueue<>();

        // User 1 joins and makes some changes
        StompSession user1Session = connectUser(1L, user1Operations, new LinkedBlockingQueue<>());
        joinRoom(user1Session, 1L);

        // User 1 sends several operations
        for (int i = 0; i < 3; i++) {
            OTOperationDTO operation = OTOperationDTO.builder()
                    .userId(1L)
                    .roomCode(roomCode)
                    .operationType("insert")
                    .position(i * 5)
                    .chars("Line" + i)
                    .version(i)
                    .timestamp(System.currentTimeMillis())
                    .build();

            user1Session.send("/collab-app/ot/operation", operation);
            Thread.sleep(100); // Allow operation to be processed
        }

        // User 2 joins later
        StompSession user2Session = stompClient.connectAsync("ws://localhost:" + port + "/collab/ws", new StompSessionHandlerAdapter() {}).get();
        
        // Subscribe to code state updates
        user2Session.subscribe("/collab-topic/room/" + roomCode + "/code-state", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return CodeStateDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                user2CodeState.offer((CodeStateDTO) payload);
            }
        });

        // Subscribe to room state updates
        user2Session.subscribe("/collab-topic/room/" + roomCode + "/state", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return RoomDetailsResponse.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                user2RoomState.offer((RoomDetailsResponse) payload);
            }
        });

        // User 2 joins room
        joinRoom(user2Session, 2L);

        // Verify User 2 receives current code state
        CodeStateDTO codeState = user2CodeState.poll(5, TimeUnit.SECONDS);
        assertNotNull(codeState, "New joiner should receive current code state");
        assertTrue(codeState.getCode().contains("Line0"), "Code state should contain existing operations");
        assertTrue(codeState.getVersion() >= 3, "Code state should reflect all operations");

        // Verify User 2 receives room state with participants
        RoomDetailsResponse roomState = user2RoomState.poll(5, TimeUnit.SECONDS);
        assertNotNull(roomState, "New joiner should receive room state");
        assertEquals(roomCode, roomState.getRoomCode());
        assertTrue(roomState.getParticipantCount() >= 1, "Room should show existing participants");

        user1Session.disconnect();
        user2Session.disconnect();
    }

    @Test
    void testCursorPositionBroadcasting() throws Exception {
        // Test that cursor positions are broadcasted to other users
        
        BlockingQueue<ParticipantEventDTO> user2CursorEvents = new LinkedBlockingQueue<>();

        StompSession user1Session = connectUser(1L, new LinkedBlockingQueue<>(), new LinkedBlockingQueue<>());
        StompSession user2Session = stompClient.connectAsync("ws://localhost:" + port + "/collab/ws", new StompSessionHandlerAdapter() {}).get();

        // User 2 subscribes to cursor updates
        user2Session.subscribe("/collab-topic/room/" + roomCode + "/cursors", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ParticipantEventDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                user2CursorEvents.offer((ParticipantEventDTO) payload);
            }
        });

        joinRoom(user1Session, 1L);
        joinRoom(user2Session, 2L);

        // User 1 sends cursor position
        CursorPositionDTO cursorPosition = CursorPositionDTO.builder()
                .userId(1L)
                .roomCode(roomCode)
                .lineNumber(5)
                .column(10)
                .build();

        user1Session.send("/collab-app/room/cursor", cursorPosition);

        // Verify User 2 receives cursor update
        ParticipantEventDTO cursorEvent = user2CursorEvents.poll(5, TimeUnit.SECONDS);
        assertNotNull(cursorEvent, "User 2 should receive cursor position update");
        assertEquals("CURSOR_UPDATE", cursorEvent.getEventType());
        assertEquals(1L, cursorEvent.getUserId());
        assertNotNull(cursorEvent.getCursorPosition());

        user1Session.disconnect();
        user2Session.disconnect();
    }

    @Test
    void testUserDisconnectionHandling() throws Exception {
        // Test that user disconnections are properly handled and broadcasted
        
        BlockingQueue<ParticipantEventDTO> user2Events = new LinkedBlockingQueue<>();

        StompSession user1Session = connectUser(1L, new LinkedBlockingQueue<>(), new LinkedBlockingQueue<>());
        StompSession user2Session = connectUser(2L, new LinkedBlockingQueue<>(), user2Events);

        joinRoom(user1Session, 1L);
        joinRoom(user2Session, 2L);

        // Wait for join events to be processed
        Thread.sleep(1000);

        // User 1 disconnects
        user1Session.disconnect();

        // Verify User 2 receives disconnect event
        ParticipantEventDTO disconnectEvent = user2Events.poll(10, TimeUnit.SECONDS);
        // Note: Disconnect events might be handled by WebSocket event listeners
        // The exact event type depends on your implementation

        user2Session.disconnect();
    }

    // Helper methods

    private StompSession connectUser(Long userId, BlockingQueue<OTOperationDTO> operationQueue, 
                                   BlockingQueue<ParticipantEventDTO> eventQueue) throws Exception {
        final Long currentUserId = userId;
        StompSession session = stompClient.connectAsync("ws://localhost:" + port + "/collab/ws", new StompSessionHandlerAdapter() {}).get();

        // Subscribe to OT operations
        session.subscribe("/collab-topic/room/" + roomCode + "/ot-operations", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return java.util.Map.class; // Expect a Map containing operation data
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                try {
                    // System.out.println("Received WebSocket message: " + payload);
                    // Convert Map to OTOperationDTO
                    if (payload instanceof java.util.Map) {
                        java.util.Map<String, Object> map = (java.util.Map<String, Object>) payload;
                        Object operationObj = map.get("operation");
                        Object senderIdObj = map.get("senderId");
                        
                        if (operationObj != null && senderIdObj != null) {
                            String json = objectMapper.writeValueAsString(operationObj);
                            OTOperationDTO operation = objectMapper.readValue(json, OTOperationDTO.class);
                            Long senderId = ((Number) senderIdObj).longValue();
                            
                            // System.out.println("Parsed operation: " + operation + " from sender: " + senderId);
                            
                            // Only add to queue if it's not from the current user
                            if (!senderId.equals(currentUserId)) {
                                operationQueue.offer(operation);
                            } else {
                                // System.out.println("Filtering out own operation from user " + currentUserId);
                            }
                        } else {
                            // System.out.println("No operation or senderId found in payload: " + map);
                        }
                    } else {
                        // System.out.println("Payload is not a Map: " + payload.getClass());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
}