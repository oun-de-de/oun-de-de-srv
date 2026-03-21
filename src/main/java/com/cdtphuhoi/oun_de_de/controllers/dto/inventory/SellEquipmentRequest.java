package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;

@Data
public class SellEquipmentRequest {

    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal expense;
}
