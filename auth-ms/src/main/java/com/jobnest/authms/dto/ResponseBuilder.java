package com.jobnest.authms.dto;

import com.jobnest.authms.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ResponseBuilder {
    public ApiResponse<List<UserDto>> buildResponseWithData(
            List<UserDto> userDto, boolean success, String message, HttpStatus status) {

        return ApiResponse.<List<UserDto>>builder()
                .data(userDto)
                .success(success)
                .message(message)
                .status(status)
                .timeStamp(LocalDateTime.now())
                .requestId(UUID.randomUUID().toString())
                .build();
    }

    public ApiResponse<String> buildResponseWithoutData(
            boolean success, String message, HttpStatus status) {

        return ApiResponse.<String>builder()
                .data(message)
                .success(success)
                .message(message)
                .status(status)
                .timeStamp(LocalDateTime.now())
                .requestId(UUID.randomUUID().toString())
                .build();
    }

    public ApiResponse<String> buildResponseWithToken(
            String token, boolean success, String message, HttpStatus status) {

        return ApiResponse.<String>builder()
                .data(token)
                .success(success)
                .message(message)
                .status(status)
                .timeStamp(LocalDateTime.now())
                .requestId(UUID.randomUUID().toString())
                .build();
    }
}