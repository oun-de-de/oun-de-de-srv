package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockTransactionReason implements ValueBasedEnum<String> {
    PURCHASE("purchase"),
    CONSUME("consume"),
    BORROW("borrow"),
    RETURN("return"),
    ;

    private final String value;
}
