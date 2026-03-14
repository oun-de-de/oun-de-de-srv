package com.cdtphuhoi.oun_de_de.controllers.dto.product;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateProductRequest {

    @NotBlank
    private String name;

    /*
     * UNIT
     */
    private UUID unitId;

    /*
     * DEFAULT PRODUCT SETTING
     */
    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal defaultPrice;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal defaultQuantity;

    private Boolean isPackagedByQuantity;
}
