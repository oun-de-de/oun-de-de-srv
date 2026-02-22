package com.cdtphuhoi.oun_de_de.services.loan.dto;

import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateLoanData {

    private BorrowerType borrowerType;

    private String borrowerId;

    private BigDecimal principalAmount;

    private Integer termMonths;

    private LocalDateTime startDate;
}
