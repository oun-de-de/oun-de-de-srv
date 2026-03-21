package com.cdtphuhoi.oun_de_de.services.cash_transaction.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateCashTransactionDetailData {

    /*
     * CHART OF ACCOUNT
     */
    private String chartOfAccountId;

    /*
     * ACCOUNT TYPE
     */
    private String accountTypeId;

    private String memo;

    private BigDecimal amount;

    /*
     * CUSTOMER
     */
    private String customerId;

    /*
     * JOURNAL CLASS
     */
    private String journalClassId;
}
