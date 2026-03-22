package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class InitStockData {

    private String refCode;

    private BigDecimal quantityOnHand;

    /*
     * optional for init stock
     */
    private BigDecimal expense;
}
