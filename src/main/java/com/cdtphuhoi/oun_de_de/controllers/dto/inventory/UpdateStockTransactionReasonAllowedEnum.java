package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import com.cdtphuhoi.oun_de_de.common.ValueBasedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UpdateStockTransactionReasonAllowedEnum implements ValueBasedEnum<String> {
    PURCHASE("purchase"),
    CONSUME("consume"),
    ;

    private final String value;
}
