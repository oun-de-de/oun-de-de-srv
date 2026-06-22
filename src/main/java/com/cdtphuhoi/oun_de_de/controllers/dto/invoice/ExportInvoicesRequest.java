package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;

import lombok.Data;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class ExportInvoicesRequest {

    @NotNull
    @NotEmpty
    @Size(max = 10000, message = "More than 10,000 invoices in this period — please select a shorter time range.")
    private List<UUID> invoiceIds;

    private String productName;

    private UUID referredBy;
}
