package com.cdtphuhoi.oun_de_de.services.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResult {
    private String id;

    private String name;

    private Date date;

    private String refNo;

    private BigDecimal quantity;

    private BigDecimal cost;

    private BigDecimal price;
}
