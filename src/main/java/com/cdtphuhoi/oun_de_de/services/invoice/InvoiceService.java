package com.cdtphuhoi.oun_de_de.services.invoice;

import com.cdtphuhoi.oun_de_de.common.InvoiceStatus;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.entities.Invoice_;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.InvoiceRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceResult;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.UpdateInvoicesData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService implements OrgManagementService {

    private final InvoiceRepository invoiceRepository;

    public Page<InvoiceResult> findBy(
        String customerId,
        LocalDateTime from,
        LocalDateTime to,
        InvoiceType invoiceType,
        InvoiceStatus invoiceStatus,
        Pageable pageable
    ) {
        var page = invoiceRepository.findAll(
            Specification.allOf(
                InvoiceSpecifications.hasCustomerId(customerId),
                InvoiceSpecifications.createBetween(from, to),
                InvoiceSpecifications.hasType(invoiceType),
                InvoiceSpecifications.hasStatus(invoiceStatus)
            ),
            pageable
        );
        return page.map(MapperHelpers.getInvoiceMapper()::toInvoiceResult);
    }

    public void updateInvoices(UpdateInvoicesData updateInvoicesData) {
        if (updateInvoicesData.getInvoiceIds().isEmpty()) {
            throw new BadRequestException("Empty invoice id");
        }
        var invoices = invoiceRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> root.get(Invoice_.ID).in(updateInvoicesData.getInvoiceIds()),
                (root, query, cb) -> cb.equal(root.get(Invoice_.TYPE), InvoiceType.INVOICE)
            )
        );
        if (invoices.size() != updateInvoicesData.getInvoiceIds().size()) {
            throw new BadRequestException(
                String.format(
                    "Updated ids not correct, given %d, found %d",
                    updateInvoicesData.getInvoiceIds().size(),
                    invoices.size()
                )
            );
        }
        var isSameCustomer = invoices.stream()
            .map(Invoice::getCustomer)
            .map(Customer::getId)
            .distinct()
            .count() == 1;
        if (!isSameCustomer) {
            throw new BadRequestException("Updated invoices must be same customer");
        }
        MapperHelpers.getInvoiceMapper().updateInvoices(invoices, updateInvoicesData);
        var updatedInvoices = invoiceRepository.saveAll(invoices);
        log.info("Update invoice successfully, affected rows {}", updatedInvoices.size());
    }
}
