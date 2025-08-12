/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.springframework.boot.SpringApplication
 *  org.springframework.boot.autoconfigure.SpringBootApplication
 *  org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration
 *  org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration
 *  org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration
 *  org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
 *  org.springframework.retry.annotation.EnableRetry
 *  org.springframework.scheduling.annotation.EnableAsync
 *  org.springframework.scheduling.annotation.EnableScheduling
 */
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraReactiveDataAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude={CassandraReactiveDataAutoConfiguration.class, CassandraAutoConfiguration.class, CassandraDataAutoConfiguration.class})
@EnableRetry
@EnableAsync
@EnableScheduling
@EnableEurekaServer
public class CollabApplication {
    public static void main(String[] args) {
        SpringApplication.run(CollabApplication.class, (String[])args);
    }
}
