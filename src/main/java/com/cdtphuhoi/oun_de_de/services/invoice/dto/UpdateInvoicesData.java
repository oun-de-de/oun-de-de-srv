package com.cdtphuhoi.oun_de_de.services.invoice.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateInvoicesData {

    private List<String> invoiceIds;

    private String customerName;

    private String type;

    private String status;
}
