package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.InvoiceStatus;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.controllers.dto.invoice.ExportInvoicesRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.invoice.UpdateInvoicesRequest;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.ExportInvoicesRequestData;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceExportLineResult;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceResult;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.UpdateInvoicesData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InvoiceMapper {
    InvoiceMapper INSTANCE = Mappers.getMapper(InvoiceMapper.class);

    @Mapping(target = "customerId", source = "invoice.customer.id")
    InvoiceResult toInvoiceResult(Invoice invoice);

    UpdateInvoicesData toUpdateInvoicesData(UpdateInvoicesRequest request);

    @ValueMapping(target = "INVOICE", source = "invoice")
    @ValueMapping(target = "RECEIPT", source = "receipt")
    InvoiceType stringToInvoiceType(String source);

    @ValueMapping(target = "OPEN", source = "open")
    @ValueMapping(target = "CLOSED", source = "closed")
    @ValueMapping(target = "OVERDUE", source = "overdue")
    InvoiceStatus stringToInvoiceStatus(String source);

    default void updateInvoices(List<Invoice> invoices, UpdateInvoicesData updateInvoicesData) {
        for (var invoice : invoices) {
            updateInvoice(invoice, updateInvoicesData);
        }
    }

    void updateInvoice(@MappingTarget Invoice invoice, UpdateInvoicesData updateInvoicesData);

    @Mapping(target = "weightRecords", source = "invoice.coupon.weightRecords")
    InvoiceExportLineResult toInvoiceExportLineResult(Invoice invoice);

    List<InvoiceExportLineResult> toListInvoiceExportLineResult(List<Invoice> invoices);

    ExportInvoicesRequestData toExportInvoicesRequestData(ExportInvoicesRequest request);
}
