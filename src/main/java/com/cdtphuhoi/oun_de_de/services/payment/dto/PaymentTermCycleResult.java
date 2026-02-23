package com.cdtphuhoi.oun_de_de.services.payment.dto;

import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTermCycleResult {

    private String id;

    private String customerId;

    private String customerName;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private PaymentTermCycleStatus status;

    private BigDecimal totalAmount;

    private BigDecimal totalPaidAmount;
}
