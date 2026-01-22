package com.cdtphuhoi.oun_de_de.exceptions;

import com.cdtphuhoi.oun_de_de.common.ErrorCode;
import org.springframework.http.HttpStatus;
import java.util.Map;

public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String resourceType, String identifier) {
        super(
            String.format("%s with identifier '%s' not found", resourceType, identifier),
            ErrorCode.RESOURCE_NOT_FOUND.getCode(),
            HttpStatus.NOT_FOUND,
            Map.of(
                "resourceType", resourceType,
                "identifier", identifier
            )
        );
    }

    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.RESOURCE_NOT_FOUND.getCode(), HttpStatus.NOT_FOUND);
    }
}