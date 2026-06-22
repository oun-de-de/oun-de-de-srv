package com.cdtphuhoi.oun_de_de.services.invoice;

import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.entities.Invoice_;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.InvoiceRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.ExportInvoicesRequestData;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceExportLineResult;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceResult;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.UpdateInvoicesData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.JoinType;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService implements OrgManagementService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;

    public Page<InvoiceResult> findBy(
        String cycleId,
        String customerId,
        LocalDateTime from,
        LocalDateTime to,
        Pageable pageable
    ) {
        var page = invoiceRepository.findAll(
            Specification.allOf(
                InvoiceSpecifications.hasCycleId(cycleId),
                InvoiceSpecifications.hasCustomerId(customerId),
                InvoiceSpecifications.createBetween(from, to)
            ),
            pageable
        );
        return page.map(MapperHelpers.getInvoiceMapper()::toInvoiceResult);
    }

    public void updateInvoices(UpdateInvoicesData updateInvoicesData) {
        // only allowed update when type is INVOICE
        var invoices = invoiceRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> root.get(Invoice_.ID).in(updateInvoicesData.getInvoiceIds())
            )
        );
        sameRequestSizeValidator(invoices, updateInvoicesData.getInvoiceIds().size());
        sameBuyerValidator(invoices);
        MapperHelpers.getInvoiceMapper().updateInvoices(invoices, updateInvoicesData);
        var updatedInvoices = invoiceRepository.saveAll(invoices);
        log.info("Update invoice successfully, affected rows {}", updatedInvoices.size());
    }

    public List<InvoiceExportLineResult> queryForExport(ExportInvoicesRequestData exportInvoicesData) {
        var invoices = invoiceRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> root.get(Invoice_.ID).in(exportInvoicesData.getInvoiceIds()),
                (root, query, cb) -> {
                    root.fetch(Invoice_.WEIGHT_RECORDS, JoinType.LEFT);
                    return null;
                }
            ),
            Sort.by(Sort.Direction.DESC, Invoice_.DATE)
        );
        sameRequestSizeValidator(invoices, exportInvoicesData.getInvoiceIds().size());

        if (StringUtils.isNotBlank(exportInvoicesData.getReferredBy())) {
            // should include this customer in the group
            var groupCustomer = customerRepository.findAll(
                Specification.allOf(
                    (root, query, cb) ->
                        cb.or(
                            cb.equal(
                                root.get(Customer_.REFERRED_BY).get(Customer_.ID),
                                exportInvoicesData.getReferredBy()
                            ),
                            cb.equal(
                                root.get(Customer_.ID),
                                exportInvoicesData.getReferredBy()
                            )
                        )
                )
            );
            var groupCustomerIds = groupCustomer.stream()
                .map(Customer::getId)
                .collect(Collectors.toSet());
            invoices = invoices.stream()
                .filter(invoice -> groupCustomerIds.contains(invoice.getCustomer().getId()))
                .toList();
        }

        var listInvoiceExportLineResult = MapperHelpers.getInvoiceMapper().toListInvoiceExportLineResult(invoices);
        if (StringUtils.isNotBlank(exportInvoicesData.getProductName())) {
            listInvoiceExportLineResult = listInvoiceExportLineResult.stream()
                .filter(line -> line.getProductName().equalsIgnoreCase(exportInvoicesData.getProductName()))
                .toList();
        }
        return listInvoiceExportLineResult;
    }

    private void sameRequestSizeValidator(List<Invoice> invoices, int size) {
        if (invoices.size() != size) {
            throw new BadRequestException(
                String.format(
                    "Request ids are not correct, given %d, found %d",
                    size,
                    invoices.size()
                )
            );
        }
    }

    private void sameBuyerValidator(List<Invoice> invoices) {
        var isSameCustomer = invoices.stream()
            .map(Invoice::getCustomer)
            .map(Customer::getId)
            .distinct()
            .count() == 1;
        if (!isSameCustomer) {
            throw new BadRequestException("Invoices must be same customer");
        }
    }
}
