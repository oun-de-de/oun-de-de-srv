package com.cdtphuhoi.oun_de_de.services.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CreateProductSettingData {
    private String productId;

    private BigDecimal price;

    private BigDecimal weight;
}
