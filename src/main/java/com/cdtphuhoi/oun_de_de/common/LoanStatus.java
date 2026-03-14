package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoanStatus implements ValueBasedEnum<String> {
    NORMAL("normal"),
    DUE("due"),
    COMPLETE("complete"),
    ;

    private final String value;
}
