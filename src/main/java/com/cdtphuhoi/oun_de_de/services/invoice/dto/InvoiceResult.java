package com.cdtphuhoi.oun_de_de.services.invoice.dto;

import com.cdtphuhoi.oun_de_de.common.InvoiceStatus;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResult {
    private String id;

    private String refNo;

    private String customerName;

    private Date date;

    private InvoiceType type;

    private InvoiceStatus status;
}
