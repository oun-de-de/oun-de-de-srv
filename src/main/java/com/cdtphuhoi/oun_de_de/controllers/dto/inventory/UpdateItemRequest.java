package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import com.cdtphuhoi.oun_de_de.common.ItemType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;

@Data
public class UpdateItemRequest {

    private String name;

    @ValueOfEnum(enumClass = ItemType.class)
    private String type;

    private UUID unitId;

    private UUID supplierId;

    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal alertThreshold;
}
