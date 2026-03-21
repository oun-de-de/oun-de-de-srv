package com.cdtphuhoi.oun_de_de.services.cash_transaction.dto;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CashTransactionResult {
    private String id;

    private String refNo;

    private CashTransactionType type;

    private LocalDateTime date;

    private String currencyId;

    private String employeeId;

    private String memo;

    private List<CashTransactionDetailResult> cashTransactionDetails;
}
