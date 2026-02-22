package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BorrowerType implements ValueBasedEnum<String> {
    EMPLOYEE("employee"),
    CUSTOMER("customer"),
    ;

    private final String value;
}
