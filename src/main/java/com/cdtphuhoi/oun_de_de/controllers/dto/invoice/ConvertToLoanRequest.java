package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_CODE_LENGTH;
import static com.cdtphuhoi.oun_de_de.common.Constants.LOAN_CODE_ERROR_MSG;
import static com.cdtphuhoi.oun_de_de.common.Constants.LOAN_CODE_REGEX;
import static com.cdtphuhoi.oun_de_de.services.loan.LoanService.DAY_IN_MONTH;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
public class ConvertToLoanRequest {

    @NotBlank
    @Size(max = DEFAULT_CODE_LENGTH)
    @Pattern(regexp = LOAN_CODE_REGEX, message = LOAN_CODE_ERROR_MSG)
    private String code;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal loanInstallmentAmount;

    @Min(0)
    @Max(DAY_IN_MONTH - 1)
    private Integer dueWarningDays;

    @NotNull
    private LocalDateTime startDate;

    private String memo;
}
