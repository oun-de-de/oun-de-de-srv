package com.cdtphuhoi.oun_de_de.services.reports.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerPaymentProjection(
    LocalDateTime paymentDate,
    String code,
    String customerName,
    BigDecimal cycleTotal,
    BigDecimal paymentAmount
) {}
