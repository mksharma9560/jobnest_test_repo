package com.jobnest.gatewayms.config;

import com.jobnest.gatewayms.filter.AuthHeaderFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class RouteConfig {

    private static final Logger log = LoggerFactory.getLogger(RouteConfig.class);
    private final AuthHeaderFilter authHeaderFilter;

    // Constructor
    public RouteConfig(AuthHeaderFilter authHeaderFilter) {
        this.authHeaderFilter = authHeaderFilter;
    }

    // Custom routing config where filter will be applied only for non-GET requests
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Job-Service
                .route("Job-Service", predicateSpec -> predicateSpec
                        .path("/api/jobs/**")
                        .and().method(HttpMethod.GET)
                        .and().header("")
                        .uri("lb://job-service"))
                .route("Job-Service", predicateSpec -> predicateSpec
                        .path("/api/jobs/**")
                        .and().header("Authorization")
                        .filters(f -> f.filter(authHeaderFilter.apply(new AuthHeaderFilter.Config())))
                        .uri("lb://job-service"))

                // Company-Service
                .route("Company-Service", predicateSpec -> predicateSpec
                        .path("/api/companies/**")
                        .and().method(HttpMethod.GET)
                        .uri("lb://company-service"))
                .route("Company-Service", predicateSpec -> predicateSpec
                        .path("/api/companies/**").and().header("Authorization")
                        .filters(f -> f.filter(authHeaderFilter.apply(new AuthHeaderFilter.Config())))
                        .uri("lb://company-service"))

                // Review-Service
                .route("Reviews-Service", predicateSpec -> predicateSpec
                        .path("/api/reviews/**")
                        .and().method(HttpMethod.GET)
                        .uri("lb://reviews-service"))
                .route("Reviews-Service", predicateSpec -> predicateSpec
                        .path("/api/reviews/**").and().header("Authorization")
                        .filters(f -> f.filter(authHeaderFilter.apply(new AuthHeaderFilter.Config())))
                        .uri("lb://reviews-service"))

                // Auth-Service
                .route("AUTH-MS", predicateSpec -> predicateSpec
                        .path("/auth/***")
                        .uri("lb://AUTH-MS"))

                .build();
    }
}