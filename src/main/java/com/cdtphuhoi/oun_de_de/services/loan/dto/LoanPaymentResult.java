package com.cdtphuhoi.oun_de_de.services.loan.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class LoanPaymentResult {
    private String id;

    private String code;

    private LocalDateTime paymentDate;

    private BigDecimal amount;
}
