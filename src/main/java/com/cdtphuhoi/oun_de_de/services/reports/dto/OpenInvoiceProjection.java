package com.cdtphuhoi.oun_de_de.services.reports.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OpenInvoiceProjection(
    LocalDateTime invoiceDate,
    String refNo,
    String customerName,
    String cycleId,
    LocalDateTime cycleStartDate,
    LocalDateTime cycleEndDate,
    BigDecimal totalPaidAmount,
    BigDecimal originalAmount
) {}
