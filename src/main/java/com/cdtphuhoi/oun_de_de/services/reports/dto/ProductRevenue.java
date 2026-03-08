package com.cdtphuhoi.oun_de_de.services.reports.dto;

import java.math.BigDecimal;

public record ProductRevenue(
    String productName,
    String unit,
    BigDecimal totalQuantity,
    BigDecimal totalAmount
) {}
