package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SellEquipmentData {
    private String refCode;

    private BigDecimal expense;
}
