package com.cdtphuhoi.oun_de_de.controllers.dto.cash_transaction;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateCashTransactionRequest {

    @NotBlank
    private String refNo;

    @NotBlank
    @ValueOfEnum(enumClass = CashTransactionType.class)
    private String type;

    private LocalDateTime date;

    /*
     * CURRENCY
     */
    private UUID currencyId;

    /*
     * USER
     */
    @NotNull
    private UUID employeeId;

    private String memo;

    /*
     * CASH TRANSACTION DETAILS
     */
    @NotNull
    @NotEmpty
    @Valid
    private List<CreateCashTransactionDetailRequest> cashTransactionDetails;
}
