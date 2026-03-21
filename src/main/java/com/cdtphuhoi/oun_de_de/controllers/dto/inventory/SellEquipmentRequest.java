package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Data
public class SellEquipmentRequest {

    @NotBlank
    private String refCode;

    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal expense;
}
