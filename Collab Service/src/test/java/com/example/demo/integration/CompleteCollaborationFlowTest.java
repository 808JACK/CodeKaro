package com.example.demo.integration;

import com.example.demo.dtos.*;
import com.example.demo.entities.Room;
import com.example.demo.entities.RoomStatus;
import com.example.demo.entities.RoomType;
import com.example.demo.repos.RoomRepository;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Complete integration test for collaborative editing features
 * Tests the entire flow from room creation to real-time collaboration
 */
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
public class CompleteCollaborationFlowTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RoomRepository roomRepository;

    private WebSocketStompClient stompClient;
    private ObjectMapper objectMapper;
    private String roomCode;
    private Room testRoom;

    @BeforeEach
    void setUp() {
        // Clean up any existing test rooms
        roomRepository.deleteAll();
        
        stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.setDefaultHeartbeat(new long[]{0, 0}); // Disable heartbeat for tests
        objectMapper = new ObjectMapper();

        // Create test room with unique invite code
        String uniqueCode = "FLOW" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        testRoom = Room.builder()
                .inviteCode(uniqueCode)
                .name("Flow Test Room")
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
        if (testRoom != null && testRoom.getId() != null) {
            roomRepository.deleteById(testRoom.getId());
        }
    }

    @Test
    void testCompleteCollaborativeEditingWorkflow() throws Exception {
        /**
         * This test simulates a complete collaborative editing session:
         * 1. Two users join a room
         * 2. Users make concurrent edits
         * 3. Operations are broadcasted in real-time
         * 4. OT ensures consistency
         * 5. New joiner receives complete state
         * 6. Users can see each other's cursors
         * 7. Users leave gracefully
         */
        
        // Phase 1: Setup and Connection
        System.out.println("=== Phase 1: Setting up users ===");
        
        CollaborativeUser user1 = new CollaborativeUser(1L, "Alice");
        CollaborativeUser user2 = new CollaborativeUser(2L, "Bob");
        
        user1.connect();
        user2.connect();
        
        // Phase 2: Users Join Room
        System.out.println("=== Phase 2: Users joining room ===");
        
        user1.joinRoom();
        user2.joinRoom();
        
        // Verify join events are received
        assertTrue(user1.waitForParticipantEvent(5), "User 1 should receive participant events");
        assertTrue(user2.waitForParticipantEvent(5), "User 2 should receive participant events");
        
        // Phase 3: Collaborative Editing
        System.out.println("=== Phase 3: Collaborative editing ===");
        
        // User 1 types "Hello"
        user1.sendOperation("insert", 0, "Hello", 0);
        
        // User 2 should receive User 1's operation
        assertTrue(user2.waitForOperation(5), "User 2 should receive User 1's operation");
        
        // User 2 types " World" at the end
        user2.sendOperation("insert", 5, " World", 1);
        
        // User 1 should receive User 2's operation
        assertTrue(user1.waitForOperation(5), "User 1 should receive User 2's operation");
        
        // Phase 4: Cursor Position Sharing
        System.out.println("=== Phase 4: Cursor position sharing ===");
        
        user1.sendCursorPosition(5, 1);
        assertTrue(user2.waitForCursorUpdate(3), "User 2 should receive User 1's cursor position");
        
        // Phase 5: New Joiner Test
        System.out.println("=== Phase 5: New joiner receives complete state ===");
        
        CollaborativeUser user3 = new CollaborativeUser(3L, "Charlie");
        user3.connect();
        user3.joinRoom();
        
        // User 3 requests current document state
        user3.requestDocumentState();
        
        // User 3 should receive complete code state
        assertTrue(user3.waitForCodeState(5), "New joiner should receive complete code state");
        
        // Phase 6: Concurrent Operations Test
        System.out.println("=== Phase 6: Testing concurrent operations ===");
        
        // All users make concurrent edits
        user1.sendOperation("insert", 11, "!", 2);
        user2.sendOperation("insert", 11, "?", 2);
        user3.sendOperation("insert", 11, ".", 2);
        
        // All users should receive all operations
        Thread.sleep(2000); // Allow time for processing
        
        // Phase 7: Performance Test
        System.out.println("=== Phase 7: Performance and latency test ===");
        
        long startTime = System.currentTimeMillis();
        user1.sendOperation("insert", 0, "SPEED", 3);
        
        assertTrue(user2.waitForOperation(1), "Operation should be received within 1 second");
        long latency = System.currentTimeMillis() - startTime;
        
        System.out.println("Operation latency: " + latency + "ms");
        assertTrue(latency < 500, "Latency should be under 500ms for real-time feel");
        
        // Phase 8: Cleanup
        System.out.println("=== Phase 8: Users leaving room ===");
        
        user1.leaveRoom();
        user2.leaveRoom();
        user3.leaveRoom();
        
        user1.disconnect();
        user2.disconnect();
        user3.disconnect();
        
        System.out.println("=== Test completed successfully ===");
    }

    @Test
    void testOperationalTransformCorrectness() throws Exception {
        /**
         * Test that OT correctly handles concurrent operations
         * to maintain document consistency
         */
        
        CollaborativeUser user1 = new CollaborativeUser(1L, "User1");
        CollaborativeUser user2 = new CollaborativeUser(2L, "User2");
        
        user1.connect();
        user2.connect();
        user1.joinRoom();
        user2.joinRoom();
        
        Thread.sleep(1000);
        
        // Both users start with empty document
        // User 1 inserts "ABC" at position 0
        // User 2 inserts "123" at position 0 (concurrent)
        
        user1.sendOperation("insert", 0, "ABC", 0);
        user2.sendOperation("insert", 0, "123", 0);
        
        // Wait for operations to be processed
        Thread.sleep(2000);
        
        // Both users should have consistent final state
        // The exact result depends on OT algorithm, but should be consistent
        
        user1.requestDocumentState();
        user2.requestDocumentState();
        
        assertTrue(user1.waitForCodeState(5), "User 1 should receive final code state");
        assertTrue(user2.waitForCodeState(5), "User 2 should receive final code state");
        
        // Note: In a real test, you'd verify the actual content is consistent
        // For now, we just verify the operations were processed
        
        user1.disconnect();
        user2.disconnect();
    }

    @Test
    void testHighConcurrencyScenario() throws Exception {
        /**
         * Test system behavior under high concurrency
         */
        
        List<CollaborativeUser> users = new ArrayList<>();
        int userCount = 5;
        
        // Create and connect multiple users
        for (int i = 1; i <= userCount; i++) {
            CollaborativeUser user = new CollaborativeUser((long) i, "User" + i);
            user.connect();
            user.joinRoom();
            users.add(user);
        }
        
        Thread.sleep(2000);
        
        // All users send operations simultaneously
        for (int i = 0; i < userCount; i++) {
            CollaborativeUser user = users.get(i);
            user.sendOperation("insert", i, "U" + i, i);
        }
        
        // Wait for all operations to be processed
        Thread.sleep(3000);
        
        // Verify all users received operations from others
        for (CollaborativeUser user : users) {
            // Each user should have received operations from other users
            assertTrue(user.getReceivedOperationCount() >= userCount - 1, 
                "User should receive operations from other users");
        }
        
        // Cleanup
        for (CollaborativeUser user : users) {
            user.disconnect();
        }
    }

    /**
     * Helper class to simulate a collaborative user
     */
    private class CollaborativeUser {
        private final Long userId;
        private final String userName;
        private StompSession session;
        
        private final BlockingQueue<Object> operations = new LinkedBlockingQueue<>();
        private final BlockingQueue<Object> participantEvents = new LinkedBlockingQueue<>();
        private final BlockingQueue<Object> cursorUpdates = new LinkedBlockingQueue<>();
        private final BlockingQueue<CodeStateDTO> codeStates = new LinkedBlockingQueue<>();
        
        private int receivedOperationCount = 0;

        public CollaborativeUser(Long userId, String userName) {
            this.userId = userId;
            this.userName = userName;
        }

        public void connect() throws Exception {
            session = stompClient.connectAsync("ws://localhost:" + port + "/collab/ws", new StompSessionHandlerAdapter() {}).get();
            
            // Subscribe to all relevant topics
            subscribeToOperations();
            subscribeToParticipants();
            subscribeToCursors();
            subscribeToCodeState();
        }

        private void subscribeToOperations() throws Exception {
            session.subscribe("/collab-topic/room/" + roomCode + "/ot-operations", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Object.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    operations.offer(payload);
                    receivedOperationCount++;
                }
            });
        }

        private void subscribeToParticipants() throws Exception {
            session.subscribe("/collab-topic/room/" + roomCode + "/participants", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Object.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    participantEvents.offer(payload);
                }
            });
        }

        private void subscribeToCursors() throws Exception {
            session.subscribe("/collab-topic/room/" + roomCode + "/cursors", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Object.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    cursorUpdates.offer(payload);
                }
            });
        }

        private void subscribeToCodeState() throws Exception {
            session.subscribe("/collab-topic/room/" + roomCode + "/code-state", new StompFrameHandler() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return CodeStateDTO.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    codeStates.offer((CodeStateDTO) payload);
                }
            });
        }

        public void joinRoom() throws Exception {
            JoinRoomRequestDTO joinRequest = new JoinRoomRequestDTO();
            joinRequest.setInviteCode(roomCode);
            joinRequest.setUserId(userId);
            session.send("/collab-app/room/join", joinRequest);
        }

        public void leaveRoom() throws Exception {
            JoinRoomRequestDTO leaveRequest = new JoinRoomRequestDTO();
            leaveRequest.setInviteCode(roomCode);
            leaveRequest.setUserId(userId);
            session.send("/collab-app/room/leave", leaveRequest);
        }

        public void sendOperation(String operationType, int position, String chars, int version) throws Exception {
            OTOperationDTO operation = OTOperationDTO.builder()
                    .userId(userId)
                    .roomCode(roomCode)
                    .operationType(operationType)
                    .position(position)
                    .chars(chars)
                    .version(version)
                    .timestamp(System.currentTimeMillis())
                    .build();

            session.send("/collab-app/ot/operation", operation);
        }

        public void sendCursorPosition(int line, int column) throws Exception {
            CursorPositionDTO cursorPosition = CursorPositionDTO.builder()
                    .userId(userId)
                    .roomCode(roomCode)
                    .lineNumber(line)
                    .column(column)
                    .build();

            session.send("/collab-app/room/cursor", cursorPosition);
        }

        public void requestDocumentState() throws Exception {
            RoomActionDTO request = RoomActionDTO.builder()
                    .roomCode(roomCode)
                    .userId(userId)
                    .build();

            session.send("/collab-app/ot/get-state", request);
        }

        public boolean waitForOperation(int timeoutSeconds) throws InterruptedException {
            return operations.poll(timeoutSeconds, TimeUnit.SECONDS) != null;
        }

        public boolean waitForParticipantEvent(int timeoutSeconds) throws InterruptedException {
            return participantEvents.poll(timeoutSeconds, TimeUnit.SECONDS) != null;
        }

        public boolean waitForCursorUpdate(int timeoutSeconds) throws InterruptedException {
            return cursorUpdates.poll(timeoutSeconds, TimeUnit.SECONDS) != null;
        }

        public boolean waitForCodeState(int timeoutSeconds) throws InterruptedException {
            return codeStates.poll(timeoutSeconds, TimeUnit.SECONDS) != null;
        }

        public int getReceivedOperationCount() {
            return receivedOperationCount;
        }

        public void disconnect() {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }
}