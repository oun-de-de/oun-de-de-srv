package com.cdtphuhoi.oun_de_de.services.loan.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UpdateLoanData {
    private BigDecimal installmentAmount;

    private Integer dueWarningDays;
}
