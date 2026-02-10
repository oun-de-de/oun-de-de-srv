package com.cdtphuhoi.oun_de_de.services.invoice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InvoiceDetailsResult {
    private String id;

    private String refNo;

    private String customerName;

    private String customerId;

    private LocalDateTime date;

    private List<InvoiceWeightRecordResult> weightRecords;
}
