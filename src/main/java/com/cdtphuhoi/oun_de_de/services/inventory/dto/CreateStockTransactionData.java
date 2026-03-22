package com.cdtphuhoi.oun_de_de.services.inventory.dto;

import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateStockTransactionData {

    private String refCode;

    private BigDecimal quantity;

    private StockTransactionReason reason;

    private String memo;

    private BigDecimal expense;
}
