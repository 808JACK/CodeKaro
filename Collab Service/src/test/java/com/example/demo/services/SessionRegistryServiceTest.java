package com.example.demo.services;

import com.example.demo.dtos.ParticipantDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SessionRegistryServiceTest {

    @InjectMocks
    private SessionRegistryService sessionRegistryService;

    private String roomCode = "TEST1234";

    @BeforeEach
    void setUp() {
        // Clear any existing sessions
        sessionRegistryService = new SessionRegistryService();
    }

    @Test
    void testUserJoinedAndLeft() {
        // Given
        Long userId = 1L;
        String userName = "TestUser";
        String userColor = "#FF0000";

        // When - User joins
        sessionRegistryService.userJoined(roomCode, userId, userName, userColor);

        // Then - User should be in the room
        assertTrue(sessionRegistryService.isUserInRoom(roomCode, userId));
        
        List<ParticipantDto> participants = sessionRegistryService.getActiveParticipants(roomCode);
        assertEquals(1, participants.size());
        assertEquals(userId, participants.get(0).getUserId());
        assertEquals(userName, participants.get(0).getUserName());
        assertEquals(userColor, participants.get(0).getUserColor());

        // When - User leaves
        sessionRegistryService.userLeft(roomCode, userId);

        // Then - User should not be in the room
        assertFalse(sessionRegistryService.isUserInRoom(roomCode, userId));
        
        List<ParticipantDto> participantsAfterLeave = sessionRegistryService.getActiveParticipants(roomCode);
        assertEquals(0, participantsAfterLeave.size());
    }

    @Test
    void testMultipleUsersInRoom() {
        // Given
        Long user1Id = 1L, user2Id = 2L, user3Id = 3L;
        String user1Name = "User1", user2Name = "User2", user3Name = "User3";
        String user1Color = "#FF0000", user2Color = "#00FF00", user3Color = "#0000FF";

        // When - Multiple users join
        sessionRegistryService.userJoined(roomCode, user1Id, user1Name, user1Color);
        sessionRegistryService.userJoined(roomCode, user2Id, user2Name, user2Color);
        sessionRegistryService.userJoined(roomCode, user3Id, user3Name, user3Color);

        // Then - All users should be in the room
        assertTrue(sessionRegistryService.isUserInRoom(roomCode, user1Id));
        assertTrue(sessionRegistryService.isUserInRoom(roomCode, user2Id));
        assertTrue(sessionRegistryService.isUserInRoom(roomCode, user3Id));

        List<ParticipantDto> participants = sessionRegistryService.getActiveParticipants(roomCode);
        assertEquals(3, participants.size());

        // When - One user leaves
        sessionRegistryService.userLeft(roomCode, user2Id);

        // Then - Only 2 users should remain
        assertTrue(sessionRegistryService.isUserInRoom(roomCode, user1Id));
        assertFalse(sessionRegistryService.isUserInRoom(roomCode, user2Id));
        assertTrue(sessionRegistryService.isUserInRoom(roomCode, user3Id));

        List<ParticipantDto> remainingParticipants = sessionRegistryService.getActiveParticipants(roomCode);
        assertEquals(2, remainingParticipants.size());
    }

    @Test
    void testUserActivityUpdate() {
        // Given
        Long userId = 1L;
        String userName = "TestUser";
        String userColor = "#FF0000";

        sessionRegistryService.userJoined(roomCode, userId, userName, userColor);

        // Get initial last activity time
        List<ParticipantDto> initialParticipants = sessionRegistryService.getActiveParticipants(roomCode);
        long initialLastActivity = initialParticipants.get(0).getLastActiveAt();

        // Wait a bit to ensure time difference
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // When - Update user activity
        sessionRegistryService.updateUserActivity(roomCode, userId);

        // Then - Last activity should be updated
        List<ParticipantDto> updatedParticipants = sessionRegistryService.getActiveParticipants(roomCode);
        long updatedLastActivity = updatedParticipants.get(0).getLastActiveAt();

        assertTrue(updatedLastActivity > initialLastActivity, 
                "Last activity should be updated to a more recent time");
    }

    @Test
    void testMultipleRooms() {
        // Given
        String room1 = "ROOM1234";
        String room2 = "ROOM5678";
        Long user1Id = 1L, user2Id = 2L;

        // When - Users join different rooms
        sessionRegistryService.userJoined(room1, user1Id, "User1", "#FF0000");
        sessionRegistryService.userJoined(room2, user2Id, "User2", "#00FF00");

        // Then - Users should be in their respective rooms only
        assertTrue(sessionRegistryService.isUserInRoom(room1, user1Id));
        assertFalse(sessionRegistryService.isUserInRoom(room1, user2Id));
        
        assertFalse(sessionRegistryService.isUserInRoom(room2, user1Id));
        assertTrue(sessionRegistryService.isUserInRoom(room2, user2Id));

        assertEquals(1, sessionRegistryService.getActiveParticipants(room1).size());
        assertEquals(1, sessionRegistryService.getActiveParticipants(room2).size());
    }

    @Test
    void testUserInMultipleRooms() {
        // Given
        String room1 = "ROOM1234";
        String room2 = "ROOM5678";
        Long userId = 1L;

        // When - Same user joins multiple rooms
        sessionRegistryService.userJoined(room1, userId, "User1", "#FF0000");
        sessionRegistryService.userJoined(room2, userId, "User1", "#FF0000");

        // Then - User should be in both rooms
        assertTrue(sessionRegistryService.isUserInRoom(room1, userId));
        assertTrue(sessionRegistryService.isUserInRoom(room2, userId));

        assertEquals(1, sessionRegistryService.getActiveParticipants(room1).size());
        assertEquals(1, sessionRegistryService.getActiveParticipants(room2).size());

        // When - User leaves one room
        sessionRegistryService.userLeft(room1, userId);

        // Then - User should only be in the second room
        assertFalse(sessionRegistryService.isUserInRoom(room1, userId));
        assertTrue(sessionRegistryService.isUserInRoom(room2, userId));

        assertEquals(0, sessionRegistryService.getActiveParticipants(room1).size());
        assertEquals(1, sessionRegistryService.getActiveParticipants(room2).size());
    }

    @Test
    void testConcurrentUserOperations() throws InterruptedException {
        // Test concurrent user joins and leaves
        int numberOfUsers = 10;
        CountDownLatch latch = new CountDownLatch(numberOfUsers);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfUsers);

        // When - Multiple users join concurrently
        for (int i = 0; i < numberOfUsers; i++) {
            final Long userId = (long) i;
            executor.submit(() -> {
                try {
                    sessionRegistryService.userJoined(roomCode, userId, "User" + userId, "#FF000" + userId);
                    sessionRegistryService.updateUserActivity(roomCode, userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all operations to complete
        assertTrue(latch.await(5, TimeUnit.SECONDS), "All users should join within 5 seconds");

        // Then - All users should be in the room
        List<ParticipantDto> participants = sessionRegistryService.getActiveParticipants(roomCode);
        assertEquals(numberOfUsers, participants.size());

        for (int i = 0; i < numberOfUsers; i++) {
            assertTrue(sessionRegistryService.isUserInRoom(roomCode, (long) i));
        }

        executor.shutdown();
    }

    @Test
    void testEmptyRoomParticipants() {
        // When - Getting participants from empty room
        List<ParticipantDto> participants = sessionRegistryService.getActiveParticipants("NONEXISTENT");

        // Then - Should return empty list
        assertNotNull(participants);
        assertEquals(0, participants.size());
    }

    @Test
    void testUserNotInRoom() {
        // Given
        Long userId = 1L;

        // When - Checking if user is in room without joining
        boolean isInRoom = sessionRegistryService.isUserInRoom(roomCode, userId);

        // Then - Should return false
        assertFalse(isInRoom);
    }

    @Test
    void testUpdateActivityForNonExistentUser() {
        // Given
        Long userId = 999L;

        // When - Updating activity for user not in room
        // This should not throw an exception
        assertDoesNotThrow(() -> {
            sessionRegistryService.updateUserActivity(roomCode, userId);
        });

        // Then - User should still not be in room
        assertFalse(sessionRegistryService.isUserInRoom(roomCode, userId));
    }

    @Test
    void testParticipantDtoFields() {
        // Given
        Long userId = 1L;
        String userName = "TestUser";
        String userColor = "#FF0000";

        // When
        sessionRegistryService.userJoined(roomCode, userId, userName, userColor);
        List<ParticipantDto> participants = sessionRegistryService.getActiveParticipants(roomCode);

        // Then
        assertEquals(1, participants.size());
        ParticipantDto participant = participants.get(0);
        
        assertEquals(userId, participant.getUserId());
        assertEquals(userName, participant.getUserName());
        assertEquals(userColor, participant.getUserColor());
        assertTrue(participant.getLastActiveAt() > 0);
        assertNotNull(participant.getJoinedAt());
    }
}