//package com.example.demo.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//import lombok.extern.slf4j.Slf4j;
//
//@Component
//@Slf4j
//public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
//
//    public AuthFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
//            String refreshToken = exchange.getRequest().getHeaders().getFirst("Refresh-Token");
//
//            // Skip auth for public endpoints
//            String path = exchange.getRequest().getPath().toString();
//            if (path.startsWith("/auth/") || path.startsWith("/api/v1/token/")) {
//                return chain.filter(exchange);
//            }
//
//            if (accessToken == null || !accessToken.startsWith("Bearer ") ||
//                refreshToken == null || !refreshToken.startsWith("Bearer ")) {
//                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                return exchange.getResponse().setComplete();
//            }
//
//            // Forward to auth service for validation
//            ServerHttpRequest validateRequest = exchange.getRequest().mutate()
//                .path("/api/v1/token/validate-both")
//                .method(exchange.getRequest().getMethod())
//                .build();
//
//            return chain.filter(exchange.mutate()
//                .request(validateRequest)
//                .build())
//                .then(chain.filter(exchange.mutate()
//                    .request(exchange.getRequest())
//                    .build()))
//                .onErrorResume(e -> {
//                    log.error("Error during token validation: {}", e.getMessage());
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return exchange.getResponse().setComplete();
//                });
//        };
//    }
//
//    public static class Config {
//        // Configuration properties if needed
//    }
//}