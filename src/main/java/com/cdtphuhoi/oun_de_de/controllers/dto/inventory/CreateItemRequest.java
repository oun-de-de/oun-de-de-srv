package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import com.cdtphuhoi.oun_de_de.common.ItemType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateItemRequest {

    @NotBlank
    private String name;

    @NotBlank
    @ValueOfEnum(enumClass = ItemType.class)
    private String type;

    /*
     * UNIT
     */
    private UUID unitId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal unitPrice;

    @Valid
    private InitStockRequest initStock;

    private BigDecimal alertThreshold;
}
