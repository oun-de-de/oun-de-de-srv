package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import com.cdtphuhoi.oun_de_de.common.ItemType;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UnitResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class InventoryItemResult {

    private String id;

    private String code;

    private String name;

    private ItemType type;

    private UnitResult unit;

    private BigDecimal quantityOnHand;

    private BigDecimal alertThreshold;
}
