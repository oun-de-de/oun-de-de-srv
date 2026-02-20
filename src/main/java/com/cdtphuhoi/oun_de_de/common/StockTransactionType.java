package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockTransactionType implements ValueBasedEnum<String> {
    IN("in"),
    OUT("out"),
    ;

    private final String value;
}
