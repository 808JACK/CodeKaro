package com.example.demo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    // CORS is handled by API Gateway - no need for service-level CORS configuration
    // This prevents duplicate CORS headers that cause browser errors
}
