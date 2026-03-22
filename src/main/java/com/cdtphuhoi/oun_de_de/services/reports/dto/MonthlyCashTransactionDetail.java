package com.cdtphuhoi.oun_de_de.services.reports.dto;

import com.cdtphuhoi.oun_de_de.common.CashTransactionReason;
import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MonthlyCashTransactionDetail(
    LocalDateTime date,
    String refNo,
    CashTransactionType type,
    CashTransactionReason reason,
    String accountNature,
    String customerName,
    String memo,
    BigDecimal amount
) {}
