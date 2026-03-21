package com.cdtphuhoi.oun_de_de.controllers.dto.cash_transaction;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateCashTransactionDetailRequest {
    /*
     * CHART OF ACCOUNT
     * UI: Cash and cash equivalents
     */
    @NotNull
    private UUID chartOfAccountId;

    /*
     * ACCOUNT TYPE
     */
    @NotNull
    private UUID accountTypeId;

    private String memo;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal amount;

    /*
     * CUSTOMER
     */
    @NotNull
    private UUID customerId;

    /*
     * JOURNAL CLASS
     */
    private UUID journalClassId;
}
