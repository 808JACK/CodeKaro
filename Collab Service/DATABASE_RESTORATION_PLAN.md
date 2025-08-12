# Database Restoration Plan

## Overview
This document outlines the systematic approach to re-enable MongoDB and Cassandra for the collaborative editor service. Currently disabled for testing purposes, these databases are essential for production deployment.

## Current State
- MongoDB: Disabled via `spring.data.mongodb.enabled=false`
- Cassandra: Disabled via `spring.data.cassandra.enabled=false`
- Using SimpleOTService with in-memory storage for testing
- CompactionService simplified to work without database dependencies

## Phase 1: MongoDB Restoration

### 1.1 Configuration Updates
- [ ] Enable MongoDB in application.properties: `spring.data.mongodb.enabled=true`
- [ ] Verify MongoDB connection settings in MongoConfig.java
- [ ] Update docker-compose.yml to include MongoDB service
- [ ] Add MongoDB health checks

### 1.2 Repository Updates
- [ ] Create MongoOTOperationRepository extending MongoRepository
- [ ] Implement full CRUD operations with MongoDB-specific queries
- [ ] Add indexing for performance (roomId, timestamp, userId)
- [ ] Implement aggregation queries for statistics

### 1.3 Service Layer Updates
- [ ] Create MongoOTService implementing OTServiceInterface
- [ ] Add MongoDB-specific operation handling
- [ ] Implement transaction support for atomic operations
- [ ] Add error handling for MongoDB connection issues

## Phase 2: Cassandra Restoration

### 2.1 Configuration Updates
- [ ] Enable Cassandra in application.properties: `spring.data.cassandra.enabled=true`
- [ ] Verify Cassandra connection settings in CassandraConfig.java
- [ ] Update docker-compose.yml to include Cassandra service
- [ ] Add Cassandra health checks

### 2.2 Repository Updates
- [ ] Create CassandraOTOperationRepository extending CassandraRepository
- [ ] Implement time-series data modeling for operations
- [ ] Add partition keys for efficient querying (roomId, time_bucket)
- [ ] Implement TTL for automatic data cleanup

### 2.3 Service Layer Updates
- [ ] Create CassandraOTService implementing OTServiceInterface
- [ ] Add Cassandra-specific operation handling
- [ ] Implement batch operations for performance
- [ ] Add error handling for Cassandra connection issues

## Phase 3: Service Selection Strategy

### 3.1 Profile-Based Configuration
```properties
# Development Profile
spring.profiles.active=dev
spring.data.mongodb.enabled=false
spring.data.cassandra.enabled=false
ot.service.implementation=simple

# Production Profile
spring.profiles.active=prod
spring.data.mongodb.enabled=true
spring.data.cassandra.enabled=true
ot.service.implementation=mongodb

# Testing Profile
spring.profiles.active=test
spring.data.mongodb.enabled=false
spring.data.cassandra.enabled=false
ot.service.implementation=simple
```

### 3.2 Conditional Bean Configuration
- [ ] Update OTServiceInterface with @ConditionalOnProperty
- [ ] Create factory pattern for service selection
- [ ] Add fallback mechanisms for service unavailability

## Phase 4: CompactionService Restoration

### 4.1 Full CompactionService Implementation
- [ ] Restore original CompactionService with database support
- [ ] Add MongoDB-specific compaction queries
- [ ] Add Cassandra-specific compaction queries
- [ ] Implement cross-database synchronization

### 4.2 Advanced Features
- [ ] Add configurable retention policies
- [ ] Implement incremental compaction
- [ ] Add compaction metrics and monitoring
- [ ] Create compaction scheduling strategies

## Phase 5: Testing Strategy

### 5.1 Integration Tests
- [ ] Create MongoDB integration tests
- [ ] Create Cassandra integration tests
- [ ] Add database failover tests
- [ ] Test service switching scenarios

### 5.2 Performance Tests
- [ ] Benchmark MongoDB vs Cassandra performance
- [ ] Test concurrent operation handling
- [ ] Measure compaction performance
- [ ] Load test with multiple databases

## Phase 6: Deployment Configuration

### 6.1 Docker Compose Updates
```yaml
services:
  mongodb:
    image: mongo:latest
    environment:
      MONGO_INITDB_ROOT_USERNAME: admin
      MONGO_INITDB_ROOT_PASSWORD: password
    ports:
      - "27017:27017"
    volumes:
      - mongodb_data:/data/db

  cassandra:
    image: cassandra:latest
    environment:
      CASSANDRA_CLUSTER_NAME: collab_cluster
    ports:
      - "9042:9042"
    volumes:
      - cassandra_data:/var/lib/cassandra

volumes:
  mongodb_data:
  cassandra_data:
```

### 6.2 Health Checks
- [ ] Add MongoDB health endpoint
- [ ] Add Cassandra health endpoint
- [ ] Create composite health check
- [ ] Add alerting for database failures

## Implementation Priority

1. **High Priority**: MongoDB restoration (primary data store)
2. **Medium Priority**: Cassandra restoration (time-series operations)
3. **Low Priority**: Advanced compaction features

## Rollback Plan

If issues arise during restoration:
1. Revert to SimpleOTService configuration
2. Disable problematic database in application.properties
3. Use in-memory storage for immediate functionality
4. Debug database issues separately

## Success Criteria

- [ ] All tests pass with database enabled
- [ ] Service starts successfully with MongoDB/Cassandra
- [ ] Real-time collaboration works with persistent storage
- [ ] Compaction service operates correctly
- [ ] Performance meets requirements
- [ ] Failover mechanisms work properly

## Notes

- Keep SimpleOTService as fallback option
- Maintain backward compatibility during transition
- Document all configuration changes
- Test thoroughly before production deployment