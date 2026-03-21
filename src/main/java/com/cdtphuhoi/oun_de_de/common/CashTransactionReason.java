package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CashTransactionReason implements ValueBasedEnum<String> {
    CASH_IN("cash_in"),
    CASH_OUT("cash_out"),
    RECEIPT("receipt"),
    GENERAL("general"),
    ;

    private final String value;
}
