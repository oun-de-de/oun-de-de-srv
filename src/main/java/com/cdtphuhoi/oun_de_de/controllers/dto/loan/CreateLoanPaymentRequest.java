package com.cdtphuhoi.oun_de_de.controllers.dto.loan;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateLoanPaymentRequest {

    @NotBlank
    private String code;

    private LocalDateTime paymentDate;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal amount;

    @NotNull
    private boolean shouldUpdateDueDate;
}
