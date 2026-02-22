package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoanInstallmentStatus implements ValueBasedEnum<String> {
    UNPAID("unpaid"),
    OVERDUE("overdue"),
    PAID("paid"),
    ;

    private final String value;
}
