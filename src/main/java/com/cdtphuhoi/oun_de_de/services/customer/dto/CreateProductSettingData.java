package com.cdtphuhoi.oun_de_de.services.customer.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateProductSettingData {
    private String productId;

    private BigDecimal price;

    private BigDecimal quantity;
}
