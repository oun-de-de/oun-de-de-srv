package com.cdtphuhoi.oun_de_de.handlers.dto;

public record FieldError(
    String field,
    Object rejectedValue,
    String message
) {}
