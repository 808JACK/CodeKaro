package com.example.demo.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;

@Configuration
public class CorsGlobalConfig {

    // Disabled - using Spring Cloud Gateway's built-in CORS configuration
    // @Bean
    // public CorsWebFilter corsWebFilter() {
    //     CorsConfiguration corsConfig = new CorsConfiguration();
    //     corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4000"));
    //     corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //     corsConfig.setAllowedHeaders(Arrays.asList(
    //             "Content-Type",
    //             "Authorization",
    //             "X-User-Id",
    //             "Accept",
    //             "Origin",
    //             "X-Requested-With",
    //             "Sec-WebSocket-Version",
    //             "Sec-WebSocket-Key",
    //             "Sec-WebSocket-Extensions",
    //             "Sec-WebSocket-Protocol"
    //     ));

    //     // ✅ expose custom headers so frontend can read them
    //     corsConfig.setExposedHeaders(Arrays.asList(
    //             "X-New-Access-Token",
    //             "X-Refresh-Expired"
    //     ));

    //     corsConfig.setAllowCredentials(true);
    //     corsConfig.setMaxAge(3600L);

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", corsConfig);

    //     return new CorsWebFilter(source);
    // }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}