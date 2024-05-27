package com.jobnest.reviewsms.exceptions;

import com.jobnest.reviewsms.helper.ErrorResponse;
import org.apache.kafka.common.KafkaException;
import org.hibernate.type.SerializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class KafkaExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaExceptionHandler.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    boolean success = false;

    @ExceptionHandler(SerializationException.class)
    public ResponseEntity<ErrorResponse> handleSerializationException(SerializationException ex, HttpServletRequest request) {
        String clientMessage = "An unexpected error occurred while processing your request. Please try again later.";
        String logMessage = "SerializationException occurred";

        return buildErrorResponse(
                ex,
                request,
                HttpStatus.BAD_REQUEST,
                logMessage,
                clientMessage);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ErrorResponse> handleTimeoutException(TimeoutException ex, HttpServletRequest request) {
        String clientMessage = "The server took too long to respond. Please try again later.";
        String logMessage = "TimeoutException occurred";

        return buildErrorResponse(
                ex,
                request,
                HttpStatus.GATEWAY_TIMEOUT,
                logMessage,
                clientMessage);
    }

    @ExceptionHandler(KafkaException.class)
    public ResponseEntity<ErrorResponse> handleKafkaException(KafkaException ex, HttpServletRequest request) {
        String clientMessage = "An unexpected error occurred while processing your request. Please try again later.";
        String logMessage = "KafkaException occurred";
        return buildErrorResponse(
                ex,
                request,
                HttpStatus.INTERNAL_SERVER_ERROR,
                logMessage,
                clientMessage);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<ErrorResponse> handleInterruptedException(InterruptedException ex, HttpServletRequest request) {
        String clientMessage = "An unexpected error occurred while processing your request. Please try again later.";
        String logMessage = "InterruptedException occurred";
        return buildErrorResponse(
                ex,
                request,
                HttpStatus.SERVICE_UNAVAILABLE,
                logMessage,
                clientMessage);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception ex,
            HttpServletRequest request,
            HttpStatus status,
            String logMessage,
            String clientMessage) {

        String apiPath = request.getRequestURI();
        ErrorResponse errorResponse = new ErrorResponse(
                clientMessage,
                success,
                status.value(),
                status.getReasonPhrase(),
                apiPath
        );

        log.error("{} \n- Success: {},\n- Status Code: {},\n- Path: {},\n- Timestamp: {},\n- Message: {}",
                logMessage, success, status.value(), apiPath, errorResponse.timestamp(), ex.getMessage());

        return new ResponseEntity<ErrorResponse>(errorResponse, status);
    }
}