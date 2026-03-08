package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailySoldProduct {

    private String productName;

    private String unit;

    private BigDecimal quantity;

    private BigDecimal amount;
}
