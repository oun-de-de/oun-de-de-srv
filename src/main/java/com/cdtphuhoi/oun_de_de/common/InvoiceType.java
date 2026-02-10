package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoiceType implements ValueBasedEnum<String> {
    INVOICE("invoice"),
    RECEIPT("receipt")
    ;

    private final String value;
}
