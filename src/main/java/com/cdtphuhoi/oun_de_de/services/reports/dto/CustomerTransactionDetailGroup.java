package com.cdtphuhoi.oun_de_de.services.reports.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CustomerTransactionDetailGroup {

    private int no;

    private String customerName;

    private List<CustomerInvoiceLine> invoices;

    private List<CustomerPaymentLine> payments;
}
