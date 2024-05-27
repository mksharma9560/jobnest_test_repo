package com.JobNest.companyms.helper;

import com.JobNest.companyms.dto.CompanyDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class ResponseBuilder {

    public ApiResponse<List<CompanyDto>> buildResponseWithData(
            List<CompanyDto> companyDtos, boolean success, String message, HttpStatus status) {

        return ApiResponse.<List<CompanyDto>>builder()
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

    public ApiResponse<CompanyDto> buildResponseWithSingleData(
            CompanyDto companyDto, boolean success, String message, HttpStatus status) {

        return ApiResponse.<CompanyDto>builder()
                .data(companyDto)
                .success(success)
                .message(message)
                .status(status)
                .timeStamp(LocalDateTime.now())
                .requestId(UUID.randomUUID().toString())
                .build();
    }
}