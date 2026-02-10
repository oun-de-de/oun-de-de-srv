package com.cdtphuhoi.oun_de_de.controllers.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateProductRequest {
    @NotBlank
    private String name;

    private LocalDateTime date;

    private String refNo;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal quantity;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal cost;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal price;

    /*
     * UNIT
     */
    private UUID unitId;

    /*
     * DEFAULT PRODUCT SETTING
     */
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal defaultPrice;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal defaultQuantity;
}
