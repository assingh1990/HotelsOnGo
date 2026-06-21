package com.hotelsongo.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

            // 🌐 Public APIs
            .route("auth-service", r -> r
                .path("/auth/**")
                .uri("lb://auth-service"))

            // 🔒 Private APIs
            .route("booking-service", r -> r
                .path("/booking/**")
                .uri("lb://booking-service"))

            .build();
    }
}