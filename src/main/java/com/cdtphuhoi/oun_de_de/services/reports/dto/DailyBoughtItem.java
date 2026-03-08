package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyBoughtItem {

    private String itemName;

    private BigDecimal expense;
}
