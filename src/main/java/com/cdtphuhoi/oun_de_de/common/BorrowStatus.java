package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BorrowStatus implements ValueBasedEnum<String> {
    BORROWED("borrowed"),
    RETURNED("returned"),
    ;

    private final String value;
}
