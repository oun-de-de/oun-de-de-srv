package com.cdtphuhoi.oun_de_de.services.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
}
