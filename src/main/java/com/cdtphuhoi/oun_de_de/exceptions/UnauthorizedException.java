package com.cdtphuhoi.oun_de_de.exceptions;

import com.cdtphuhoi.oun_de_de.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApplicationException {

    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED.getCode(), HttpStatus.UNAUTHORIZED);
    }
}
