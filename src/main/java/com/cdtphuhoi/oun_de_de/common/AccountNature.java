package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountNature implements ValueBasedEnum<String> {
    ASSET("asset"),
    LIABILITY("liability"),
    EQUITY("equity"),
    REVENUE("revenue"),
    EXPENSE("expense"),
    ;

    private final String value;
}
