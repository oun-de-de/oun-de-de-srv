package com.cdtphuhoi.oun_de_de.controllers.dto.inventory;

import com.cdtphuhoi.oun_de_de.common.ItemType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;

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

    private BigDecimal quantityOnHand;

    private BigDecimal alertThreshold;
}
