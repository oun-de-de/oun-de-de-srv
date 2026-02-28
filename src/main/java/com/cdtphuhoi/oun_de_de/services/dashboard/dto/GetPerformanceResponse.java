package com.cdtphuhoi.oun_de_de.services.dashboard.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class GetPerformanceResponse {
    private BigDecimal income;

    // TODO: update later, need confirm from customer
    private BigDecimal expenses;
}
