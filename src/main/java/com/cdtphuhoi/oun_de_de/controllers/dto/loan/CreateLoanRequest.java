package com.cdtphuhoi.oun_de_de.controllers.dto.loan;

import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateLoanRequest {

    @NotBlank
    @ValueOfEnum(enumClass = BorrowerType.class)
    private String borrowerType;

    @NotNull
    private UUID borrowerId;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal principalAmount;

    @NotNull
    @Min(value = 1)
    private Integer termMonths;

    @NotNull
    private LocalDateTime startDate;
}
