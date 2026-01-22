package com.cdtphuhoi.oun_de_de.exceptions;

import org.springframework.http.HttpStatus;
import java.util.Map;

public class BusinessValidationException extends ApplicationException {

    public BusinessValidationException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public BusinessValidationException(String message, String errorCode, Map<String, Object> details) {
        super(message, errorCode, HttpStatus.UNPROCESSABLE_ENTITY, details);
    }
}
