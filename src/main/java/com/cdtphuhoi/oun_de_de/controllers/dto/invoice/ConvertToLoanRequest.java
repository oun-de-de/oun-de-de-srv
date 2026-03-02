package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Data
public class ConvertToLoanRequest {

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal loanInstallmentAmount;

    @NotNull
    private LocalDateTime startDate;
}
