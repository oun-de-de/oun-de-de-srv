package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentTermCycleStatus implements ValueBasedEnum<String> {
    OPEN("open"),
    CLOSED("closed"),
    OVERDUE("overdue"),
    ;

    private final String value;

}
