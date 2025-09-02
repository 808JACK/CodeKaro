package com.example.demo.filter;

import com.example.demo.Util.TokenValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    private final TokenValidation tokenValidation;
    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();
        log.info("[AuthFilter] Incoming {} {}", method, path);

        // Handle OPTIONS preflight requests - let them pass through
        if (HttpMethod.OPTIONS.equals(method)) {
            return chain.filter(exchange);
        }

        if (path.startsWith("/auth/login") || path.startsWith("/auth/signup") || path.startsWith("/auth/verify-otp") || path.startsWith("/auth/refreshAccessToken")) {
            log.info("[AuthFilter] Allowing unauthenticated access to: {}", path);
            return chain.filter(exchange);
        }

        // Allow unauthenticated GET access to static uploads
        if (path.startsWith("/uploads/") && HttpMethod.GET.equals(method)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("[AuthFilter] Missing or invalid Authorization header for {} {}", method, path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        log.info("[AuthFilter] Token present; validating... {} {}", method, path);
        if (tokenValidation.isValid(token) && !tokenValidation.isExpired(token)) {
            System.out.println("goingtotake");
            log.info("[AuthFilter] Token valid; forwarding {} {}", method, path);
            System.out.println("expiredandtaking");
            return chain.filter(exchange); // ✅ valid access token
        } else {
            Long userId = tokenValidation.extractUserId(token);
            if (userId == null) {
                log.warn("[AuthFilter] Could not extract userId from token; unauthorized {} {}", method, path);
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            log.info("[AuthFilter] Access token expired/invalid; attempting refresh for user {}", userId);
            return webClientBuilder.build()
                    .get()
                    .uri("http://AUTH-SERVICE/auth/refreshAT/{userId}", userId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .flatMap(newAccessToken -> {
                        if ("REFRESH_EXPIRED".equals(newAccessToken)) {
                            log.warn("[AuthFilter] Refresh token expired for user {}; signaling frontend to login", userId);
                            // refresh token expired → tell frontend to redirect to login
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            exchange.getResponse().getHeaders().set("X-Refresh-Expired", "true");
                            return exchange.getResponse().setComplete();
                        } else if (newAccessToken != null && !newAccessToken.isBlank()) {
                            log.info("[AuthFilter] Got new access token for user {}; forwarding request", userId);
                            // valid new access token
                            ServerWebExchange updatedExchange = exchange.mutate()
                                    .request(r -> r.headers(h -> h.set(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken)))
                                    .build();

                            exchange.getResponse().getHeaders().set("X-New-Access-Token", newAccessToken);
                            return chain.filter(updatedExchange);
                        } else {
                            log.warn("[AuthFilter] Refresh returned empty token for user {}; unauthorized", userId);
                            // fallback → unauthorized
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    })
                    .onErrorResume(error -> {
                        log.error("[AuthFilter] Error during token refresh for user {}: {}", userId, error.getMessage());
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }
    }
    @Override
    public int getOrder() {
        return 1; // Run after CORS filter
    }
}