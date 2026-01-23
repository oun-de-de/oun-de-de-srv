package com.cdtphuhoi.oun_de_de.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.cdtphuhoi.oun_de_de.common.ErrorCode;
import com.cdtphuhoi.oun_de_de.handlers.dto.ApiError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Set;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

class GlobalExceptionHandlerTest {

    private HttpServletRequest request;
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    void handleMethodArgumentNotValid_ReturnsApiError() {
        var bindingResult = mock(BindingResult.class);
        var fieldError = new FieldError("object", "field", "rejected", false, null, null, "msg");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        var ex = new MethodArgumentNotValidException(null, bindingResult);

        var response = handler.handleMethodArgumentNotValid(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var error = (ApiError) response.getBody();
        assertNotNull(error);
        assertEquals(ErrorCode.VALIDATION_FAILED.getCode(), error.errorCode());
        assertEquals(1, error.fieldErrors().size());
        assertNotNull(error.traceId());
    }

    @Test
    void handleConstraintViolation_ReturnsApiError() {
        var violation = mock(ConstraintViolation.class);
        var path = mock(Path.class);
        when(path.toString()).thenReturn("pathStr");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getInvalidValue()).thenReturn("bad");
        when(violation.getMessage()).thenReturn("must not be null");
        var ex = mock(ConstraintViolationException.class);
        when(ex.getConstraintViolations()).thenReturn(Set.of(violation));

        var response = handler.handleConstraintViolation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        var error = response.getBody();
        assertNotNull(error);
        assertEquals(ErrorCode.CONSTRAINT_VIOLATION.getCode(), error.errorCode());
        assertEquals("/api/test", error.instance());
        assertEquals(1, error.fieldErrors().size());
        assertNotNull(error.traceId());
    }

    @Test
    void handleAccessDenied_ReturnsApiError() {
        var ex = new AccessDeniedException("denied");
        var response = handler.handleAccessDenied(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        var error = response.getBody();
        assertNotNull(error);
        assertEquals(ErrorCode.ACCESS_DENIED.getCode(), error.errorCode());
        assertEquals("/api/test", error.instance());
        assertNotNull(error.traceId());
    }

    @Test
    void handleAuthenticationError_ReturnsApiError() {
        var ex = mock(AuthenticationException.class);
        when(ex.getMessage()).thenReturn("unauth");
        var response = handler.handleAuthenticationError(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        var error = response.getBody();
        assertNotNull(error);
        assertEquals(ErrorCode.UNAUTHORIZED.getCode(), error.errorCode());
        assertEquals("/api/test", error.instance());
        assertNotNull(error.traceId());
    }

    @Test
    void handleAllUncaughtException_ReturnsApiError() {
        var ex = new RuntimeException("fail");
        var response = handler.handleAllUncaughtException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        var error = response.getBody();
        assertNotNull(error);
        assertEquals("INTERNAL_ERROR", error.errorCode());
        assertEquals("/api/test", error.instance());
        assertNotNull(error.traceId());
    }
}
