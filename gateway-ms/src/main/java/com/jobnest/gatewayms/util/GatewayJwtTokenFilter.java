//package com.jobnest.gatewayms.helper;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class GatewayJwtTokenFilter extends OncePerRequestFilter {
//    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayJwtTokenFilter.class);
//
//    private GatewayJwtUtil jwtUtil;
//    public GatewayJwtTokenFilter(GatewayJwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        LOGGER.info("*** Inside doFilterInternal method with request: {}", request);
//        String token = null;
//
//        //  if Authorization header does not exist, then skip this filter
//        //  and continue to execute next filter class
//        if (!hasAuthorizationHeader(request)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        //get token from request
//        token = getAccessToken(request);
//
//        // if token is not valid, then skip this filter and continue to execute next filter class.
//        // This means authentication is not successful since token is invalid.
//        if (!jwtUtil.validateAccessToken(token)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // Authorization header exists, token is valid and not expired. So, we can authenticate the request
//        setAuthenticationContext(token, request);
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean hasAuthorizationHeader(HttpServletRequest request) {
//        LOGGER.info("*** Inside hasAuthorizationBearer method ***");
//        String header = request.getHeader("Authorization");
//        if (header == null || !header.startsWith("Bearer")) {
//            LOGGER.info("*** Request header isEmpty || not starting with Bearer ***");
//            return false;
//        }
//        LOGGER.info("*** Request has header: {}", header);
//        return true;
//    }
//
//    private String getAccessToken(HttpServletRequest request) {
//        LOGGER.info("*** Inside getAccessToken method ***");
//        String header = request.getHeader("Authorization");
//        String token = header.split(" ")[1].trim();
//        LOGGER.info("*** AccessToken fetched from request is: {}", token);
//        return token;
//    }
//
//    private void setAuthenticationContext(String token, HttpServletRequest request) {
//        LOGGER.info("*** Inside setAuthenticationContext method ***");
//        String username = jwtUtil.getUsername(token);
//        LOGGER.info("*** Username for provided token is: {}", username);
//
//        // initializing UsernamePasswordAuthenticationToken with its 3 parameter constructor because
//        // it sets super.setAuthenticated(true); in that constructor.
//        UsernamePasswordAuthenticationToken upassToken =
//                new UsernamePasswordAuthenticationToken(username, null, null);
//        LOGGER.info("*** Object of UsernamePasswordAuthenticationToken is: {}", upassToken.toString());
//        upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//        // finally, give the authentication token to Spring Security Context
//        SecurityContextHolder.getContext().setAuthentication(upassToken);
//        LOGGER.info("*** Token and Request set in AuthenticationContext ***");
//    }
//}