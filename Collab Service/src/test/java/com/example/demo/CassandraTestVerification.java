package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple verification test to ensure Cassandra test configuration works
 */
@SpringBootTest
@TestPropertySource(properties = {
    "cassandra.enabled=true",
    "mongodb.enabled=false",
    "eureka.client.enabled=false"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CassandraTestVerification {

    @Test
    void testCassandraConfigurationLoads() {
        // This test simply verifies that the Spring context can load with Cassandra enabled
        // If this passes, it means our Cassandra test configuration is valid
        assertTrue(true, "Cassandra test configuration loaded successfully");
    }

    @Test
    void testBasicAssertions() {
        // Basic test to verify JUnit is working
        assertEquals(2, 1 + 1);
        assertNotNull("test");
        assertTrue(true);
    }
}