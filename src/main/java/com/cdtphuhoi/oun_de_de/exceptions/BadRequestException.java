package com.cdtphuhoi.oun_de_de.exceptions;

import com.cdtphuhoi.oun_de_de.common.ErrorCode;
import org.springframework.http.HttpStatus;
import java.util.Map;

public class BadRequestException extends ApplicationException {

    public BadRequestException(String message) {
        super(message, ErrorCode.BAD_REQUEST.getCode(), HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, Map<String, Object> details) {
        super(message, ErrorCode.BAD_REQUEST.getCode(), HttpStatus.BAD_REQUEST, details);
    }
}
