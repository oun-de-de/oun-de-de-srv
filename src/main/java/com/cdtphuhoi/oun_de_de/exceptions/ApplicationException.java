package com.cdtphuhoi.oun_de_de.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.Map;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Map<String, Object> details;

    protected ApplicationException(
        String message,
        String errorCode,
        HttpStatus httpStatus,
        Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.details = details != null ? details : Map.of();
    }

    protected ApplicationException(
        String message,
        String errorCode,
        HttpStatus httpStatus) {
        this(message, errorCode, httpStatus, null);
    }

}

// 404 - Resource not found


