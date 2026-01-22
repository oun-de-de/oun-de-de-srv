package com.cdtphuhoi.oun_de_de.handler.dto;

public record FieldError(
    String field,
    Object rejectedValue,
    String message
) {}
