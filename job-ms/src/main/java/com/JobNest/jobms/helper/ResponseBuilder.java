package com.JobNest.jobms.helper;

import com.JobNest.jobms.dto.JobDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ResponseBuilder {

    public ApiResponse<List<JobDto>> buildResponseWithData(
            List<JobDto> jobDtos, boolean success, String message, HttpStatus status) {

        return ApiResponse.<List<JobDto>>builder()
                .data(jobDtos)
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

    public ApiResponse<JobDto> buildResponseWithSingleData(
            JobDto companyDto, boolean success, String message, HttpStatus status) {

        return ApiResponse.<JobDto>builder()
                .data(companyDto)
                .success(success)
                .message(message)
                .status(status)
                .timeStamp(LocalDateTime.now())
                .requestId(UUID.randomUUID().toString())
                .build();
    }
}