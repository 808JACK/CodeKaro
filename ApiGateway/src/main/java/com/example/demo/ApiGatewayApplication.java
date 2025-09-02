package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		System.out.println("🚀 Starting API Gateway...");
		SpringApplication.run(ApiGatewayApplication.class, args);
		System.out.println("✅ API Gateway started successfully!");
	}

}