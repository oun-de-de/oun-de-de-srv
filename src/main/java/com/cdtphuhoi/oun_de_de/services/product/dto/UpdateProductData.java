package com.cdtphuhoi.oun_de_de.services.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class UpdateProductData {

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
