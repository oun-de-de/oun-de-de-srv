package com.cdtphuhoi.oun_de_de.controllers.dto.customer;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateProductSettingRequest {
    /*
     * PRODUCT
     */
    @NotNull
    private UUID productId;

    private BigDecimal price;

    private BigDecimal weight;
}
