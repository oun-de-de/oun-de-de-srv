package com.cdtphuhoi.oun_de_de.exceptions;

import com.cdtphuhoi.oun_de_de.common.ErrorCode;
import org.springframework.http.HttpStatus;
import java.util.Map;

// 403 - Forbidden
public class ForbiddenException extends ApplicationException {

    public ForbiddenException(String message) {
        super(message, ErrorCode.ACCESS_DENIED.getCode(), HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String action, String resource) {
        super(
            String.format("You don't have permission to %s this %s", action, resource),
            ErrorCode.ACCESS_DENIED.getCode(),
            HttpStatus.FORBIDDEN,
            Map.of("action", action, "resource", resource)
        );
    }
}
