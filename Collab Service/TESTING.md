# Collaborative Editor Testing Guide

This document explains how to run and understand the test cases for the collaborative editor functionality.

## 🎯 Test Coverage

Our test suite covers all critical aspects of collaborative editing:

### 1. **Broadcasting Tests** 📡
- **What it tests**: Operations are sent to all connected participants
- **Why it matters**: Ensures all users see changes made by others
- **Test files**: `CollaborativeWebSocketTest.java`, `CompleteCollaborationFlowTest.java`

### 2. **Real-time Communication Tests** ⚡
- **What it tests**: Messages are delivered with low latency (< 1 second)
- **Why it matters**: Provides smooth, responsive collaborative experience
- **Test files**: `WebSocketIntegrationTest.java`, `CompleteCollaborationFlowTest.java`

### 3. **Operational Transform (OT) Tests** 🔄
- **What it tests**: Concurrent operations are handled correctly
- **Why it matters**: Maintains document consistency when multiple users edit simultaneously
- **Test files**: `SimpleOTServiceTest.java`, `CollaborativeWebSocketTest.java`

### 4. **New Joiner Tests** 👋
- **What it tests**: New users receive complete room state when joining
- **Why it matters**: Ensures new participants see current document and all participants
- **Test files**: `CompleteCollaborationFlowTest.java`, `WebSocketIntegrationTest.java`

### 5. **Session Management Tests** 👥
- **What it tests**: User join/leave events are handled properly
- **Why it matters**: Maintains accurate participant lists and handles disconnections
- **Test files**: `SessionRegistryServiceTest.java`

### 6. **Cursor Position Tests** 👆
- **What it tests**: Cursor positions are shared between users
- **Why it matters**: Users can see where others are editing
- **Test files**: `CollaborativeWebSocketTest.java`

## 🚀 Running the Tests

### Option 1: Run All Tests (Recommended)
```bash
# Navigate to Collab Service directory
cd "Collab Service"

# Run all tests using Maven
./mvnw test

# Or run specific test classes
./mvnw test -Dtest=CompleteCollaborationFlowTest
```

### Option 2: Run Test Categories

#### Unit Tests (Fast)
```bash
./mvnw test -Dtest=SimpleOTServiceTest,SessionRegistryServiceTest
```

#### Integration Tests (Slower, but comprehensive)
```bash
./mvnw test -Dtest=CompleteCollaborationFlowTest,CollaborativeWebSocketTest
```

#### WebSocket Tests
```bash
./mvnw test -Dtest=WebSocketIntegrationTest,CollaborativeWebSocketTest
```

### Option 3: Run from IDE
1. Open your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. Navigate to `src/test/java/com/example/demo/`
3. Right-click on any test class and select "Run Tests"

## 📊 Understanding Test Results

### ✅ Successful Test Output
```
🚀 Starting Collaborative Editor Test Suite
============================================

📊 Test Results Summary
=======================
Tests found: 25
Tests started: 25
Tests successful: 25
Tests failed: 0
Tests skipped: 0

✅ All tests passed! Collaborative editor is working correctly.
```

### ❌ Failed Test Analysis

If tests fail, check these common issues:

1. **WebSocket Connection Failures**
   - Ensure the service is running on the correct port
   - Check if WebSocket endpoint is configured properly

2. **Database Issues**
   - Verify PostgreSQL is running and accessible
   - Check if test database is properly configured

3. **Timing Issues**
   - Some tests might fail due to network latency
   - Increase timeout values if needed

4. **Port Conflicts**
   - Ensure no other services are using the test ports
   - Check if multiple test instances are running

## 🔧 Test Configuration

### Test Properties
Tests use these configurations (in `application-test.properties`):
```properties
# Disable external dependencies for testing
cassandra.enabled=false
mongodb.enabled=false

# Use in-memory database for tests
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop

# Enable debug logging for troubleshooting
logging.level.com.example.demo=DEBUG
```

### Test Data
- Tests create temporary rooms with invite codes like `TEST1234`, `INTEG123`
- Mock users with IDs 1L, 2L, 3L are used
- All test data is cleaned up after each test

## 🧪 Test Scenarios Explained

### Scenario 1: Basic Collaboration
```
1. User A joins room
2. User B joins room
3. User A types "Hello"
4. User B receives "Hello" in real-time
5. User B types " World"
6. User A receives " World" in real-time
7. Final document: "Hello World"
```

### Scenario 2: Concurrent Editing
```
1. Both users start with empty document
2. User A types "ABC" at position 0
3. User B types "123" at position 0 (simultaneously)
4. OT system resolves conflict
5. Both users end up with consistent document
```

### Scenario 3: New Joiner
```
1. User A joins and types "Hello World"
2. User B joins later
3. User B immediately sees "Hello World"
4. User B sees User A in participant list
```

## 🐛 Troubleshooting

### Common Issues and Solutions

1. **Tests timeout**
   ```bash
   # Increase timeout in test configuration
   # Or run tests individually to isolate issues
   ./mvnw test -Dtest=SimpleOTServiceTest
   ```

2. **WebSocket connection refused**
   ```bash
   # Check if service is running
   curl http://localhost:8092/actuator/health
   
   # Verify WebSocket endpoint
   curl -i -N -H "Connection: Upgrade" -H "Upgrade: websocket" \
        -H "Sec-WebSocket-Version: 13" -H "Sec-WebSocket-Key: test" \
        http://localhost:8092/ws
   ```

3. **Database connection issues**
   ```bash
   # Check PostgreSQL status
   pg_isready -h localhost -p 5430
   
   # Verify database exists
   psql -h localhost -p 5430 -U postgres -l
   ```

## 📈 Performance Benchmarks

Our tests verify these performance criteria:

- **Message Latency**: < 500ms for real-time feel
- **Concurrent Users**: Support 10+ simultaneous users
- **Operation Throughput**: Handle 100+ operations per second
- **Memory Usage**: Stable under continuous load

## 🎯 Next Steps

After running tests successfully:

1. **Deploy to staging** and run integration tests
2. **Load test** with realistic user numbers
3. **Monitor** real-world performance metrics
4. **Add more test scenarios** as features are added

## 📞 Support

If you encounter issues with tests:

1. Check the console output for specific error messages
2. Review the test logs in `target/surefire-reports/`
3. Verify all dependencies are properly configured
4. Ensure the service is running before integration tests

---

**Happy Testing! 🚀**

The comprehensive test suite ensures your collaborative editor works reliably for real users.