package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemType implements ValueBasedEnum<String> {
    CONSUMABLE("consumable"),
    EQUIPMENT("equipment"),
    ;

    private final String value;
}
