package com.cdtphuhoi.oun_de_de.services.product.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateProductData {
    private String name;

    private LocalDateTime date;

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
