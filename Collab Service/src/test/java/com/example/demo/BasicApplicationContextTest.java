package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "cassandra.enabled=false",
    "mongodb.enabled=false",
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"
})
public class BasicApplicationContextTest {

    @Test
    void contextLoads() {
        // This test will pass if the ApplicationContext loads successfully
    }
}