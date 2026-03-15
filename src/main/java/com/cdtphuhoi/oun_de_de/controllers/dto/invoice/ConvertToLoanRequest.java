package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;

import static com.cdtphuhoi.oun_de_de.services.loan.LoanService.DAY_IN_MONTH;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Data
public class ConvertToLoanRequest {

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal loanInstallmentAmount;

    @Min(0)
    @Max(DAY_IN_MONTH - 1)
    private Integer dueWarningDays;

    @NotNull
    private LocalDateTime startDate;
}
