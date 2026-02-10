package com.cdtphuhoi.oun_de_de.controllers.dto.customer;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateProductSettingRequest {
    /*
     * PRODUCT
     */
    @NotNull
    private UUID productId;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal price;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal quantity;
}
