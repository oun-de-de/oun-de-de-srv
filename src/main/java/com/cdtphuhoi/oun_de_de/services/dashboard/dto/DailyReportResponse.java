package com.cdtphuhoi.oun_de_de.services.dashboard.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class DailyReportResponse {

    private Date date;

    private BigDecimal income;

    private BigDecimal expense;
}
