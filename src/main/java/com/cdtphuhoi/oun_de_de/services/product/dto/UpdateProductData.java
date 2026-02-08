package com.cdtphuhoi.oun_de_de.services.product.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class UpdateProductData {
    private String name;

    private Date date;

    private String refNo;

    private BigDecimal quantity;

    private BigDecimal cost;

    private BigDecimal price;

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
