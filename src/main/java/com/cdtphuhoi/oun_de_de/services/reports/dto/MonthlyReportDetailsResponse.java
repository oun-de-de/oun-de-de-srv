package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class MonthlyReportDetailsResponse {

    private List<MonthlyReportLine> lines;
}
