package com.cdtphuhoi.oun_de_de.controllers.dto.loan;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_CODE_LENGTH;
import static com.cdtphuhoi.oun_de_de.services.loan.LoanService.DAY_IN_MONTH;
import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CreateLoanRequest {

    @NotBlank
    @Size(max = DEFAULT_CODE_LENGTH)
    private String code;

    @NotBlank
    @ValueOfEnum(enumClass = BorrowerType.class)
    private String borrowerType;

    @NotNull
    private UUID borrowerId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal principalAmount;

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
