package com.cdtphuhoi.oun_de_de.services.cash_transaction.dto;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateCashTransactionData {

    private String refNo;

    private CashTransactionType type;

    private LocalDateTime date;

    /*
     * CURRENCY
     */
    private String currencyId;

    /*
     * USER
     */
    private String employeeId;

    private String memo;

    /*
     * CASH TRANSACTION DETAILS
     */
    private List<CreateCashTransactionDetailData> cashTransactionDetails;
}
