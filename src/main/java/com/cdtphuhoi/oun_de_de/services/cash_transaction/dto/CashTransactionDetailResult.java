package com.cdtphuhoi.oun_de_de.services.cash_transaction.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CashTransactionDetailResult {
    private String id;

    private String chartOfAccountId;

    private String accountTypeId;

    private String memo;

    private BigDecimal amount;

    private String customerId;

    private String journalClassId;
}
