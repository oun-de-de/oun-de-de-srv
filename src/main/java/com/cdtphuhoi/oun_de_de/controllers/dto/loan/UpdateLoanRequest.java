package com.cdtphuhoi.oun_de_de.controllers.dto.loan;

import static com.cdtphuhoi.oun_de_de.services.loan.LoanService.DAY_IN_MONTH;
import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Data
public class UpdateLoanRequest {

    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal installmentAmount;

    @Min(0)
    @Max(DAY_IN_MONTH - 1)
    private Integer dueWarningDays;
}
