package com.cdtphuhoi.oun_de_de.services.payment.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ConvertToLoanData {

    private BigDecimal loanInstallmentAmount;

    private LocalDateTime startDate;
}
