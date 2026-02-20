package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateStockTransactionRequest {

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal quantity;

    @NotBlank
    @ValueOfEnum(enumClass = StockTransactionReason.class)
    private String reason;

    private String memo;
}
