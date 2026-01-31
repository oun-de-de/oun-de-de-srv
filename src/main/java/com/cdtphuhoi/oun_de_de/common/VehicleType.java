package com.cdtphuhoi.oun_de_de.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VehicleType implements ValueBasedEnum<String> {
    TRUCK("truck"),
    TUK_TUK("tuk_tuk"),
    OTHERS("others")
    ;

    private final String value;
}
