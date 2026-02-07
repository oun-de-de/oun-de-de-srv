package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UnitType implements ValueBasedEnum<String> {
    COUNT("count"),
    LENGTH("length"),
    WEIGHT("weight"),
    VOLUME("volume"),
    TIME("time")
    ;
    private final String value;
}