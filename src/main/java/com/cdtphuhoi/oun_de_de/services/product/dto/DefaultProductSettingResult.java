package com.cdtphuhoi.oun_de_de.services.product.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class DefaultProductSettingResult {
    private String id;

    private BigDecimal price;

    private BigDecimal quantity;
}
