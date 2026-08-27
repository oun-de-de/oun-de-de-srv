package com.cdtphuhoi.oun_de_de.services.reports.dto;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashTransactionDetailProjection(
    LocalDateTime date,
    String refNo,
    CashTransactionType type,
    String journalClassName,
    String memo,
    BigDecimal amount
) {}
