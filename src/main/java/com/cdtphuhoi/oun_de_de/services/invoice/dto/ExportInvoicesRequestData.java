package com.cdtphuhoi.oun_de_de.services.invoice.dto;

import lombok.Data;
import java.util.List;

@Data
public class ExportInvoicesRequestData {
    private List<String> invoiceIds;
}
