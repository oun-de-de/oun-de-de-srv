package com.cdtphuhoi.oun_de_de.services.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ProductSettingResult {
    private String productId;

    private String customerId;

    private BigDecimal price;

    private BigDecimal quantity;
}
