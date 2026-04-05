package com.cdtphuhoi.oun_de_de.services.loan.dto;

import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.common.LoanStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanResult {
    private String id;

    private BorrowerType borrowerType;

    private String borrowerId;

    private String borrowerName;

    private BigDecimal principalAmount;

    private BigDecimal paidAmount;

    private BigDecimal installmentAmount;

    private Integer dueWarningDays;

    private LocalDateTime dueDate;

    private LocalDateTime startDate;

    private LoanStatus status;

    private LocalDateTime createAt;

    private String memo;
}
