package com.example.demo;

/**
 * Test suite runner that provides information about available tests
 * Use Maven or your IDE to run specific test classes
 */
public class TestSuiteRunner {
    
    public static void main(String[] args) {
        System.out.println("🚀 Collaborative Editor Test Suite");
        System.out.println("============================================");
        System.out.println("📋 Available Test Classes:");
        System.out.println();
        
        System.out.println("🔧 UNIT TESTS:");
        System.out.println("   • SimpleOTServiceTest - In-memory OT operations");
        System.out.println("   • CassandraOTServiceTest - Cassandra-based OT operations");
        System.out.println("   • SessionRegistryServiceTest - Session management");
        System.out.println();
        
        System.out.println("🌐 WEBSOCKET TESTS:");
        System.out.println("   • CollaborativeWebSocketTest - Real-time collaboration");
        System.out.println();
        
        System.out.println("🔗 INTEGRATION TESTS:");
        System.out.println("   • CompleteCollaborationFlowTest - End-to-end flow");
        System.out.println("   • CassandraNewUserJoinTest - New user join scenarios");
        System.out.println();
        
        System.out.println("🚀 HOW TO RUN:");
        System.out.println("   # Run all tests");
        System.out.println("   mvn test");
        System.out.println();
        System.out.println("   # Run specific test class");
        System.out.println("   mvn test -Dtest=CassandraOTServiceTest");
        System.out.println();
        System.out.println("   # Run only Cassandra tests");
        System.out.println("   mvn test -Pcassandra-tests");
        System.out.println();
        System.out.println("   # Run specific test method");
        System.out.println("   mvn test -Dtest=CassandraNewUserJoinTest#testNewUserJoinsRoomWithExistingOperations");
        System.out.println("============================================");
    }
}