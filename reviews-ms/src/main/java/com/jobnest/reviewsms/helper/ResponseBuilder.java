package com.jobnest.reviewsms.helper;

import com.jobnest.reviewsms.dto.ReviewDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ResponseBuilder {

    public ApiResponse<List<ReviewDto>> buildResponseWithData(
            List<ReviewDto> companyDtos, boolean success, String message, HttpStatus status) {

        return ApiResponse.<List<ReviewDto>>builder()
                .data(companyDtos)
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

    public ApiResponse<ReviewDto> buildResponseWithSingleData(
            ReviewDto reviewDto, boolean success, String message, HttpStatus status) {

        return ApiResponse.<ReviewDto>builder()
                .data(reviewDto)
                .success(success)
                .message(message)
                .status(status)
                .timeStamp(LocalDateTime.now())
                .requestId(UUID.randomUUID().toString())
                .build();
    }
}