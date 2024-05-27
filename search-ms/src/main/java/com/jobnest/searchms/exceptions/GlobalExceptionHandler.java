package com.jobnest.searchms.exceptions;

import com.jobnest.searchms.helper.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    boolean success = false;
    /* ************************* CUSTOM EXCEPTIONS BEGIN******************************* */

    //ApplicationException
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationException ex, HttpServletRequest request) {
        String clientMessage = "An unexpected error occurred while processing your request. Please try again later.";
        String logMessage = "ApplicationException occurred";
        return buildErrorResponse(
                ex,
                request,
                HttpStatus.INTERNAL_SERVER_ERROR,
                logMessage,
                clientMessage);
    }

    //ExternalServiceException
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<?> handleExternalServiceException(ExternalServiceException ex, HttpServletRequest request) {
        String clientMessage = "The service is currently unavailable. Please try again later.";
        String logMessage = "ExternalServiceException occurred";
        return buildErrorResponse(
                ex,
                request,
                HttpStatus.SERVICE_UNAVAILABLE,
                logMessage,
                clientMessage
        );
    }

    //    ************************* CUSTOM EXCEPTIONS END *******************************

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        String clientMessage = "The request could not be processed due to invalid input. Please check and try again.";
        String logMessage = "ConstraintViolationException occurred";
        return buildErrorResponse(
                ex,
                request,
                HttpStatus.BAD_REQUEST,
                logMessage,
                clientMessage
        );
    }

    // MethodArgumentNotValidException
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest webRequest
    ) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        // getting fields from the error
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        // formatting error message
        StringBuilder errorMessageBuilder = new StringBuilder();
        errors.forEach((field, message) -> errorMessageBuilder.append(message).append("; "));

        String logMessage = "MethodArgumentNotValidException occurred - " + errorMessageBuilder;
        String clientMessage = "One or more parameters are invalid. Please check your input and try again.";
        ResponseEntity<ErrorResponse> errorResponse = buildErrorResponse(
                ex,
                req,
                HttpStatus.BAD_REQUEST,
                logMessage,
                clientMessage
        );
        // Create a new ResponseEntity<Object> using errorResponse
        ResponseEntity<Object> responseEntity =
                ResponseEntity
                        .status(errorResponse.getStatusCode())
                        .headers(errorResponse.getHeaders())
                        .body(errorResponse.getBody());

        return responseEntity;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String logMessage = "HttpMessageNotReadableException occurred - " + ex.getMessage();
        String clientMessage = "Request body is empty. Please check your input data and try again.";
        ResponseEntity<ErrorResponse> errorResponse = buildErrorResponse(
                ex,
                req,
                HttpStatus.BAD_REQUEST,
                logMessage,
                clientMessage
        );
        // Create a new ResponseEntity<Object> using errorResponse
        ResponseEntity<Object> responseEntity =
                ResponseEntity
                        .status(errorResponse.getStatusCode())
                        .headers(errorResponse.getHeaders())
                        .body(errorResponse.getBody());

        return responseEntity;
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String logMessage = "handleHttpRequestMethodNotSupported occurred - " + ex.getMessage();
        String clientMessage = "Request url is in incorrect format. Please check your input and try again.";
        ResponseEntity<ErrorResponse> errorResponse = buildErrorResponse(
                ex,
                req,
                HttpStatus.METHOD_NOT_ALLOWED,
                logMessage,
                clientMessage
        );
        // Create a new ResponseEntity<Object> using errorResponse
        ResponseEntity<Object> responseEntity =
                ResponseEntity
                        .status(errorResponse.getStatusCode())
                        .headers(errorResponse.getHeaders())
                        .body(errorResponse.getBody());

        return responseEntity;
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String logMessage = "handleTypeMismatch occurred - " + ex.getMessage();
        String clientMessage = "Requested method type not supported. Please check your request type and try again.";
        ResponseEntity<ErrorResponse> errorResponse = buildErrorResponse(
                ex,
                req,
                HttpStatus.BAD_REQUEST,
                logMessage,
                clientMessage
        );
        // Create a new ResponseEntity<Object> using errorResponse
        ResponseEntity<Object> responseEntity =
                ResponseEntity
                        .status(errorResponse.getStatusCode())
                        .headers(errorResponse.getHeaders())
                        .body(errorResponse.getBody());

        return responseEntity;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex, HttpServletRequest request) {
        throw new ApplicationException(ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception ex,
            HttpServletRequest request,
            HttpStatus status,
            String logMessage,
            String clientMessage) {

        String apiPath = request.getRequestURI();
        ErrorResponse errorResponse = new ErrorResponse(
                clientMessage, success, status.value(), status.getReasonPhrase(), apiPath
        );
        log.error("{} \n- Success: {},\n- Status Code: {},\n- Path: {},\n- Timestamp: {},\n- Message: {}",
                logMessage, success, status.value(), apiPath, errorResponse.timestamp(), ex.getMessage());

        return new ResponseEntity<ErrorResponse>(errorResponse, status);
    }
}