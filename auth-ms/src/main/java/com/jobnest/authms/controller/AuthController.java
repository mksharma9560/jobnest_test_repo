package com.jobnest.authms.controller;

import com.jobnest.authms.dto.ApiResponse;
import com.jobnest.authms.dto.AuthRequest;
import com.jobnest.authms.dto.ResponseBuilder;
import com.jobnest.authms.dto.UserDto;
import com.jobnest.authms.entities.User;
import com.jobnest.authms.service.UserService;
import com.jobnest.authms.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authManager;
    private final ResponseBuilder responseBuilder;

    private boolean success;
    private String message;
    private HttpStatus httpStatus;

    // Constructor
    public AuthController(JwtUtil jwtUtil, UserService userService, AuthenticationManager authManager, ResponseBuilder responseBuilder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.authManager = authManager;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody User user) {
        log.info("Executing registerUser()");
        success = userService.saveUser(user);
        message = success ? "User registered successfully" : "User already registered";
        httpStatus = success ? HttpStatus.CREATED : HttpStatus.NOT_ACCEPTABLE;

        ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest request) {
        try {
            log.info("Executing authenticateUser()");
            UsernamePasswordAuthenticationToken upassAuthtoken = new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            );
            // JwtAuthenticationProvider will be called internally
            Authentication authenticate = authManager.authenticate(upassAuthtoken);

            success = authenticate.isAuthenticated();

            String username = String.valueOf(authenticate.getPrincipal());
            String accessToken = jwtUtil.generateAccessToken(username);

            message = success ? "Authentication successful" : "Authentication failed";
            httpStatus = success ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

            ApiResponse<String> response = responseBuilder.buildResponseWithToken(accessToken, success, message, httpStatus);
            return new ResponseEntity<>(response, httpStatus);
        } catch (BadCredentialsException ex) {
            log.warn("*** BadCredentialsException - {}", ex.getMessage());
            success = false;
            message = "Username or Password is incorrect. Please check and try again";
            httpStatus = HttpStatus.UNAUTHORIZED;

            ApiResponse<String> response = responseBuilder.buildResponseWithoutData(success, message, httpStatus);
            return new ResponseEntity<>(response, httpStatus);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> userController() {
        log.info("Executing registerUser()");
        List<UserDto> userDtos = userService.getUsers();
        success = !userDtos.isEmpty();
        message = success ? "Users data fetched successfully" : "Users data not available";
        httpStatus = success ? HttpStatus.OK : HttpStatus.NOT_FOUND;

        ApiResponse<List<UserDto>> response = responseBuilder.buildResponseWithData(userDtos, success, message, httpStatus);
        return new ResponseEntity<>(response, httpStatus);
    }
}