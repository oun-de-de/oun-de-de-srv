package com.cdtphuhoi.oun_de_de.services.cash_transaction.dto;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CashTransactionFlattenResult {

    private String id;

    private String refNo;

    private CashTransactionType type;

    private String reason;

    private LocalDateTime date;

    private String currency;

    private String memo;

    private BigDecimal amount;
}
