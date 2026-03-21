package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CashTransactionType implements ValueBasedEnum<String> {
    DEBIT("debit"),
    CREDIT("credit"),
    ;

    private final String value;
}
