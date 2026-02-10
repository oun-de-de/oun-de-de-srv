package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;

import lombok.Data;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class ListInvoiceDetailsRequest {

    @NotNull
    @NotEmpty
    private List<UUID> invoiceIds;
}
