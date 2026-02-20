package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateItemData {

    private String name;

    private String type;

    /*
     * UNIT
     */
    private String unitId;

    private BigDecimal quantityOnHand;

    private BigDecimal alertThreshold;
}
