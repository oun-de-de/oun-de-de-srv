package com.cdtphuhoi.oun_de_de.services.customer.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaymentTermResult {
    private Integer duration;

    private LocalDateTime startDate;
}
