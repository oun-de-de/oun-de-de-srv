package com.cdtphuhoi.oun_de_de.services.reports.dto;

import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.common.StockTransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventoryStockReportLine {
    private String itemName;

    private String itemCode;

    private BigDecimal quantity;

    private StockTransactionType type;

    private StockTransactionReason reason;

    private LocalDateTime createdAt;
}
