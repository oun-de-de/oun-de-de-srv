package com.cdtphuhoi.oun_de_de.exceptions;

import com.cdtphuhoi.oun_de_de.common.ErrorCode;
import org.springframework.http.HttpStatus;
import java.util.Map;

public class ConflictException extends ApplicationException {

    public ConflictException(String message) {
        super(message, ErrorCode.RESOURCE_ALREADY_EXISTS.getCode(), HttpStatus.CONFLICT);
    }

    public ConflictException(String resourceType, String field, String value) {
        super(
            String.format("%s with %s '%s' already exists", resourceType, field, value),
            ErrorCode.RESOURCE_ALREADY_EXISTS.getCode(),
            HttpStatus.CONFLICT,
            Map.of(
                "resourceType", resourceType,
                "field", field,
                "value", value
            )
        );
    }
}
