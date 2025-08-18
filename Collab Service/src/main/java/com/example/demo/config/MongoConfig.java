/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mongodb.ConnectionString
 *  com.mongodb.MongoClientSettings
 *  com.mongodb.client.MongoClient
 *  com.mongodb.client.MongoClients
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.ComponentScan$Filter
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.context.annotation.FilterType
 *  org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
 *  org.springframework.data.mongodb.repository.config.EnableMongoRepositories
 *  org.springframework.retry.annotation.EnableRetry
 */
package com.example.demo.config;

import com.example.demo.repos.ChatMessageRepository;
import com.example.demo.repos.ContestSubmissionRepository;
import com.example.demo.repos.RoomCompactedCodeStateRepository;
import com.example.demo.repos.UserRecentRoomsRepository;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@ConditionalOnProperty(name={"mongodb.enabled"}, havingValue="true", matchIfMissing=false)
@EnableRetry
@EnableMongoRepositories(basePackages={"com.example.demo.repos"}, includeFilters={@ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value={ContestSubmissionRepository.class})})
public class MongoConfig
extends AbstractMongoClientConfiguration {
    
    public MongoConfig() {
        System.out.println("MongoConfig constructor called - MongoDB is being configured!");
    }
    @Value(value="${spring.data.mongodb.uri}")
    private String mongoUri;
    @Value(value="${spring.data.mongodb.database:Collabdb}")
    private String databaseName;

    protected String getDatabaseName() {
        return this.databaseName;
    }

    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(this.mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().applyConnectionString(connectionString).applyToSocketSettings(builder -> builder.connectTimeout(10000L, TimeUnit.MILLISECONDS).readTimeout(15000L, TimeUnit.MILLISECONDS)).applyToConnectionPoolSettings(builder -> builder.maxWaitTime(15000L, TimeUnit.MILLISECONDS).maxConnectionIdleTime(60000L, TimeUnit.MILLISECONDS).maxConnectionLifeTime(120000L, TimeUnit.MILLISECONDS).maxSize(50).minSize(5)).applyToServerSettings(builder -> builder.heartbeatFrequency(10000L, TimeUnit.MILLISECONDS).minHeartbeatFrequency(5000L, TimeUnit.MILLISECONDS)).applyToClusterSettings(builder -> builder.serverSelectionTimeout(15000L, TimeUnit.MILLISECONDS)).build();
        return MongoClients.create((MongoClientSettings)mongoClientSettings);
    }
}
