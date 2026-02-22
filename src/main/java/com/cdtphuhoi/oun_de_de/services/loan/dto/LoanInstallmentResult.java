package com.cdtphuhoi.oun_de_de.services.loan.dto;

import com.cdtphuhoi.oun_de_de.common.LoanInstallmentStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanInstallmentResult {
    private String id;

    private String loanId;

    private Integer monthIndex;

    private LocalDateTime dueDate;

    private BigDecimal amount;

    private LoanInstallmentStatus status;

    private LocalDateTime paidAt;
}
