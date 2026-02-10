package com.cdtphuhoi.oun_de_de.services.invoice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InvoiceExportLineResult {

    private String refNo;

    private String customerName;

    private LocalDateTime date;

    private String productName;

    private String unit;

    private BigDecimal pricePerProduct;

    private BigDecimal quantityPerProduct;

    private BigDecimal quantity;

    private BigDecimal amount;

    private BigDecimal total;

    private String memo;

    private BigDecimal paid;

    private BigDecimal balance;
}
