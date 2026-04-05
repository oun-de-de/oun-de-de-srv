package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateItemData {

    private String name;

    private String type;

    private String unitId;

    private String supplierId;

    private BigDecimal unitPrice;

    private BigDecimal alertThreshold;
}
