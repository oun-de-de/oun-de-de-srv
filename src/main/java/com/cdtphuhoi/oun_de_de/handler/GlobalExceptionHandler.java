package com.cdtphuhoi.oun_de_de.handler;

import com.cdtphuhoi.oun_de_de.common.ErrorCode;
import com.cdtphuhoi.oun_de_de.exceptions.ApplicationException;
import com.cdtphuhoi.oun_de_de.handler.dto.ApiError;
import com.cdtphuhoi.oun_de_de.handler.dto.FieldError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.nio.file.AccessDeniedException;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiError> handleApplicationException(
        ApplicationException ex,
        HttpServletRequest request) {

        var traceId = generateTraceId();

        log.warn("Application exception [traceId={}]: {} - {}",
            traceId, ex.getErrorCode(), ex.getMessage());

        var error = ApiError.builder()
            .status(ex.getHttpStatus())
            .detail(ex.getMessage())
            .errorCode(ex.getErrorCode())
            .instance(request.getRequestURI())
            .traceId(traceId)
            .details(ex.getDetails())
            .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request) {

        var traceId = generateTraceId();

        var fieldErrors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> new FieldError(
                error.getField(),
                error.getRejectedValue(),
                error.getDefaultMessage()
            ))
            .toList();

        log.warn("Validation failed [traceId={}]: {} field errors",
            traceId, fieldErrors.size());

        var error = ApiError.builder()
            .status(HttpStatus.BAD_REQUEST)
            .detail(ErrorCode.VALIDATION_FAILED.getDesc())
            .errorCode(ErrorCode.VALIDATION_FAILED.getCode())
            .traceId(traceId)
            .fieldErrors(fieldErrors)
            .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
        ConstraintViolationException ex,
        HttpServletRequest request) {

        var traceId = generateTraceId();

        var fieldErrors = ex.getConstraintViolations()
            .stream()
            .map(violation -> new FieldError(
                violation.getPropertyPath().toString(),
                violation.getInvalidValue(),
                violation.getMessage()
            ))
            .toList();

        log.warn("Constraint violation [traceId={}]: {}", traceId, ex.getMessage());

        var error = ApiError.builder()
            .status(HttpStatus.BAD_REQUEST)
            .detail(ErrorCode.CONSTRAINT_VIOLATION.getDesc())
            .errorCode(ErrorCode.CONSTRAINT_VIOLATION.getCode())
            .instance(request.getRequestURI())
            .traceId(traceId)
            .fieldErrors(fieldErrors)
            .build();

        return ResponseEntity.badRequest().body(error);
    }

    // Handle access denied (Spring Security)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(
        AccessDeniedException ex,
        HttpServletRequest request) {

        var traceId = generateTraceId();
        log.warn("Access denied [traceId={}]: {}", traceId, ex.getMessage());

        var error = ApiError.builder()
            .status(HttpStatus.FORBIDDEN)
            .detail(ErrorCode.ACCESS_DENIED.getDesc())
            .errorCode(ErrorCode.ACCESS_DENIED.getCode())
            .instance(request.getRequestURI())
            .traceId(traceId)
            .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // Handle all unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(
        Exception ex,
        HttpServletRequest request) {

        var traceId = generateTraceId();

        // Log full stack trace for unexpected errors
        log.error("Unexpected error [traceId={}]: {}", traceId, ex.getMessage(), ex);

        // Don't expose internal details to client
        var error = ApiError.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .detail("An unexpected error occurred. Please try again later.")
            .errorCode("INTERNAL_ERROR")
            .instance(request.getRequestURI())
            .traceId(traceId)
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}