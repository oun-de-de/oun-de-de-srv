package com.cdtphuhoi.oun_de_de.services.invoice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreatePaymentData {

    private LocalDateTime paymentDate;

    private BigDecimal amount;
}
