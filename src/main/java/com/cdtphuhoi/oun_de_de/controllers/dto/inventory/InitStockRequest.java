package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class InitStockRequest {

    @NotBlank
    private String refCode;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal quantityOnHand;

    /*
     * optional for init stock
     */
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal expense;
}
