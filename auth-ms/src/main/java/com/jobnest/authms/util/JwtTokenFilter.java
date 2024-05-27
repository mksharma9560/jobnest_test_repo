package com.jobnest.authms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenFilter.class);
    private final JwtUtil jwtUtil;

    public JwtTokenFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("Executing doFilterInternal()");
        String token = null;

        //  if request don't have authorization header, skip this filter and continue to next filter
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        token = fetchAccessToken(request);

        // if token invalid then authentication failed, skip this filter and continue to next filter
        if (!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // If auth header exists and token is valid & not expired then authenticate the request
        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        log.info("Executing hasAuthorizationBearer()");
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            log.warn("Request header is empty or not starting with Bearer");
            return false;
        }
        return true;
    }

    private String fetchAccessToken(HttpServletRequest request) {
        log.info("Executing getAccessToken()");
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        log.info("Token fetched successfully");
        return token;
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        log.info("Executing setAuthenticationContext()");
        String username = jwtUtil.getUsername(token);

        UsernamePasswordAuthenticationToken upassToken =
                new UsernamePasswordAuthenticationToken(username, null, null);
        upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(upassToken);
        log.info("Token and Request set in AuthenticationContext");
    }
}