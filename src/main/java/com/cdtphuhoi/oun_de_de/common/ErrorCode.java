package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST("BAD_REQUEST", "Bad request"),
    VALIDATION_FAILED("VALIDATION_FAILED", "Validation failed. Check 'fieldErrors' for details."),
    CONSTRAINT_VIOLATION("CONSTRAINT_VIOLATION", "Constraint validation failed"),
    MALFORMED_JSON("MALFORMED_JSON", "Malformed JSON request body"),
    RESOURCE_ALREADY_EXISTS("RESOURCE_ALREADY_EXISTS", "Resource already exists"),
    ACCESS_DENIED("ACCESS_DENIED", "You don't have permission to access this resource"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Resource not found"),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized"),
    ;

    private final String code;

    private final String desc;
}
