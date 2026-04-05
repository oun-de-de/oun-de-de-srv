package com.cdtphuhoi.oun_de_de.services.loan.dto;

import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CreateLoanData {

    private String code;

    private BorrowerType borrowerType;

    private String borrowerId;

    private BigDecimal principalAmount;

    private BigDecimal loanInstallmentAmount;

    private Integer dueWarningDays;

    private LocalDateTime startDate;

    private String memo;
}
