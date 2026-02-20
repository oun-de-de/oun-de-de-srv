package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.common.StockTransactionType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockTransactionResult {
    private String id;

    private String itemId;

    private BigDecimal quantity;

    private StockTransactionType type;

    private StockTransactionReason reason;

    private String memo;

    private LocalDateTime createdAt;

    private String equipmentBorrowId;

    private String createdById;
}
