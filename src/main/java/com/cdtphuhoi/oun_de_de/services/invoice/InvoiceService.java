package com.cdtphuhoi.oun_de_de.services.invoice;

import com.cdtphuhoi.oun_de_de.common.InvoiceStatus;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.entities.Invoice_;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.InvoiceRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService implements OrgManagementService {

    private final InvoiceRepository invoiceRepository;

    public Page<InvoiceResult> findBy(
        String optCustomerId,
        LocalDateTime optFrom,
        LocalDateTime optTo,
        InvoiceType optInvoiceType,
        InvoiceStatus optStatus,
        Pageable pageable
    ) {
        Specification<Invoice> customerIdSpec =
            (root, query, cb) ->
                Optional.ofNullable(optCustomerId)
                    .map(
                        customerId -> cb.equal(
                            root.get(Invoice_.CUSTOMER).get(Customer_.ID),
                            customerId
                        )
                    )
                    .orElse(null);

        Specification<Invoice> dateSpec =
            (root, query, cb) -> {
                var date = root.get(Invoice_.date);
                if (optFrom != null && optTo != null) {
                    return cb.between(
                        date,
                        optFrom,
                        optTo
                    );
                }
                if (optFrom != null) {
                    return cb.greaterThanOrEqualTo(date, optFrom);
                }
                if (optTo != null) {
                    return cb.lessThanOrEqualTo(date, optTo);
                }
                return null;
            };
        var page = invoiceRepository.findAll(
            customerIdSpec
                .and(dateSpec),
            pageable
        );
        return page.map(MapperHelpers.getInvoiceMapper()::toInvoiceResult);
    }
}
