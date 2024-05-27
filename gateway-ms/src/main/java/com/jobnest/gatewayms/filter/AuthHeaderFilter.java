package com.jobnest.gatewayms.filter;

import com.jobnest.gatewayms.util.GatewayJwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class AuthHeaderFilter extends AbstractGatewayFilterFactory<AuthHeaderFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(AuthHeaderFilter.class);

    private final GatewayJwtUtil jwtUtil;

    // Constructor injection
    public AuthHeaderFilter(GatewayJwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("Executing AuthHeaderFilter.apply()");
            HttpHeaders headers = exchange.getRequest().getHeaders();
            List<String> authorizationHeaders = headers.get("AUTHORIZATION");

            if (authorizationHeaders == null || authorizationHeaders.isEmpty()) {
                log.error("Authorization headers are missing in the request");
                String message = "Authorization headers are missing in the request. Please include the 'Authorization' header with the bearer token.";
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
            }

            // Iterate through the list of authorization headers
            for (String authorizationHeader : authorizationHeaders) {
                // Check if the header starts with "Bearer "
                if (!authorizationHeader.startsWith("Bearer ")) {
                    log.error("Authorization header does not have prefix as Bearer");
                    String message = "The 'Authorization' header is missing or incorrect. Please include a valid Bearer token in the 'Authorization' header.";
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
                }

                // Extract access token
                String accessToken = authorizationHeader.substring("Bearer ".length()).trim();
                if (accessToken.isEmpty()) {
                    log.error("Access Token is empty");
                    String message = "The access token in the 'Authorization' header is empty. Please provide a valid access token.";
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
                }

                // Validate the access token
                boolean isTokenValid = jwtUtil.validateAccessToken(accessToken);
                if (!isTokenValid) {
                    log.error("Invalid Access Token");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Access token, Please check and try again");
                }

                // Add JWT token to the request headers before forwarding the request downstream
                exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            }
            // Proceed with the chain by calling chain.filter(exchange)
            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}