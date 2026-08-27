package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CustomerPaymentLine {

    private LocalDateTime date;

    private String refNo;

    private BigDecimal openAmount;

    private BigDecimal received;
}
