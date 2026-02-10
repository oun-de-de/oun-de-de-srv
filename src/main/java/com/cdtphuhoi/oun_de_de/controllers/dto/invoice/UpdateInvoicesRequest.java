package com.cdtphuhoi.oun_de_de.controllers.dto.invoice;

import com.cdtphuhoi.oun_de_de.common.InvoiceStatus;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class UpdateInvoicesRequest {

    @NotNull
    @NotEmpty
    private List<UUID> invoiceIds;

    private String customerName;

    @ValueOfEnum(enumClass = InvoiceType.class)
    private String type;

    @ValueOfEnum(enumClass = InvoiceStatus.class)
    private String status;
}
