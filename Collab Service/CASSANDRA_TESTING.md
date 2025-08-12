# 🗄️ Cassandra Testing Guide

This document explains how to test the **Cassandra-based collaborative editing functionality**, specifically focusing on the scenario where **new users join rooms and receive all previous changes**.

## 🎯 **What We're Testing**

### **Key Scenario: New User Joins Room**
When a new user joins a collaborative editing room, they should:
1. **Receive current document state** - reconstructed from all previous operations
2. **Get correct version number** - to sync properly with other users  
3. **Be able to make new edits** - that get properly versioned and broadcasted
4. **See real-time updates** - from other users continuing to edit

## 🧪 **Test Structure**

### **1. Unit Tests (`CassandraOTServiceTest`)**
Tests the core Cassandra OT service functionality:
- ✅ `testNewUserJoinsRoom_ReceivesAllPreviousChanges()` - Document reconstruction
- ✅ `testNewUserJoinsRoom_GetsCorrectOperationCount()` - Version tracking
- ✅ `testNewUserJoinsRoom_CanSyncFromSpecificVersion()` - Partial sync
- ✅ `testConcurrentUsersAndNewUserJoins()` - Multi-user scenarios
- ✅ `testCompleteNewUserJoinFlow()` - End-to-end flow

### **2. Integration Tests (`CassandraNewUserJoinTest`)**
Tests the complete WebSocket flow with Cassandra:
- ✅ `testNewUserJoinsRoomWithExistingOperations()` - Full WebSocket flow
- ✅ `testMultipleNewUsersJoinSequentially()` - Sequential joins
- ✅ `testNewUserJoinsEmptyRoom()` - Edge case handling

## 🚀 **Running the Tests**

### **Option 1: Verify Test Setup**
```bash
# First, verify Cassandra test setup works
mvn test -Dtest=CassandraTestVerification

# This should pass and show "✅ Cassandra test verification passed"
```

### **Option 2: Run Individual Cassandra Tests**
```bash
# Run unit tests
mvn test -Dtest=CassandraOTServiceTest

# Run integration tests  
mvn test -Dtest=CassandraNewUserJoinTest

# Run complete test suite (if JUnit Suite dependencies work)
mvn test -Dtest=TestSuiteRunner
```

### **Option 3: Run Only Cassandra Tests**
```bash
# Use the cassandra-tests profile
mvn test -Pcassandra-tests

# This runs only:
# - CassandraOTServiceTest
# - CassandraNewUserJoinTest
```

### **Option 4: Run Specific Test Methods**
```bash
# Test new user join scenario specifically
mvn test -Dtest=CassandraOTServiceTest#testNewUserJoinsRoom_ReceivesAllPreviousChanges

# Test WebSocket integration
mvn test -Dtest=CassandraNewUserJoinTest#testNewUserJoinsRoomWithExistingOperations
```

## 🔧 **Test Configuration**

### **Cassandra Tests Use:**
```properties
cassandra.enabled=true
mongodb.enabled=false
eureka.client.enabled=false
```

### **SimpleOT Tests Use:**
```properties
cassandra.enabled=false
mongodb.enabled=false
eureka.client.enabled=false
```

## 📊 **Test Scenarios Covered**

### **1. Document Reconstruction**
```java
// Given: Room has operations: "Hello ", "World", "!"
// When: New user joins
// Then: Receives "Hello World!" as current state
```

### **2. Version Synchronization**
```java
// Given: Room has 5 operations (version 5)
// When: New user joins
// Then: Gets version=5, can make operation at version 6
```

### **3. Concurrent Editing**
```java
// Given: User1 and User2 are editing
// When: User3 joins
// Then: Sees merged changes from both users
```

### **4. WebSocket Flow**
```java
// Given: User1 makes changes via WebSocket
// When: User2 joins via WebSocket  
// Then: Receives CodeStateDTO with all changes
```

## 🎯 **Key Test Assertions**

### **Document State Verification**
```java
// Verify reconstructed code contains all changes
assertTrue(currentCode.contains("Hello World!"));
assertEquals("Expected final state", currentCode);
```

### **Version Tracking**
```java
// Verify version numbers are correct
assertEquals(3L, operationCount);
assertTrue(codeState.getVersion() >= 3);
```

### **WebSocket Message Flow**
```java
// Verify new user receives code state
CodeStateDTO codeState = codeStates.poll(5, TimeUnit.SECONDS);
assertNotNull(codeState, "New user should receive current code state");
```

## 🔍 **Debugging Tests**

### **Enable Debug Logging**
```properties
logging.level.com.example.demo=DEBUG
logging.level.org.springframework.messaging=DEBUG
```

### **Common Issues**
1. **Cassandra not enabled** - Check `cassandra.enabled=true` in test properties
2. **WebSocket timeout** - Increase timeout in `poll(5, TimeUnit.SECONDS)`
3. **Operation ordering** - Cassandra tests rely on proper operation sequencing
4. **Mock repository** - Unit tests use mocked repositories, integration tests use real ones

## 📈 **Test Coverage**

The tests cover:
- ✅ **Core OT Operations** - Insert, delete, replace
- ✅ **Document Reconstruction** - From operation history
- ✅ **Version Management** - Proper versioning and sync
- ✅ **WebSocket Integration** - Real-time messaging
- ✅ **Multi-user Scenarios** - Concurrent editing
- ✅ **Edge Cases** - Empty rooms, late joins
- ✅ **Error Handling** - Invalid operations, missing rooms

## 🎉 **Success Criteria**

Tests pass when:
1. **New users receive complete document state** from Cassandra
2. **Version numbers are correctly managed** across operations
3. **WebSocket messages flow properly** between users
4. **Concurrent editing works** without data loss
5. **Performance is acceptable** for document reconstruction

Run these tests to ensure your Cassandra-based collaborative editing works perfectly for new users joining rooms! 🚀