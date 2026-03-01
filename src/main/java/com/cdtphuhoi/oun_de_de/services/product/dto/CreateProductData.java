package com.cdtphuhoi.oun_de_de.services.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateProductData {
    private String name;

    /*
     * UNIT
     */
    private String unitId;

    /*
     * DEFAULT PRODUCT SETTING
     */
    private BigDecimal defaultPrice;

    private BigDecimal defaultQuantity;
}
