package com.cdtphuhoi.oun_de_de.jobs.dto;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import java.math.BigDecimal;

public record MonthlyCashTransactionForBalance(
    CashTransactionType type,
    BigDecimal amount
) {
}
