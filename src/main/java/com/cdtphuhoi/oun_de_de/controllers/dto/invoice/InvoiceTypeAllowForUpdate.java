package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;


import com.cdtphuhoi.oun_de_de.common.ValueBasedEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoiceTypeAllowForUpdate implements ValueBasedEnum<String> {
    RECEIPT("receipt"),
    ;

    private final String value;
}
