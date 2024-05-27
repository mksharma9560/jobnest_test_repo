package com.jobnest.reviewsms.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record ErrorResponse(
        String message, boolean success, int statusCode, String statusMessage,
        String path, String timestamp, UUID requestId
) {
    public ErrorResponse(
            String message, boolean success, int statusCode,
            String statusMessage, String path
    ) {
        this(
                message, success, statusCode, statusMessage, path,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                UUID.randomUUID()
        );
    }
}