package com.cdtphuhoi.oun_de_de.services.invoice;

import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.InvoiceRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService implements OrgManagementService {
    private final InvoiceRepository invoiceRepository;

    public Page<InvoiceResult> findBy(Pageable pageable) {
        var page = invoiceRepository.findAll(pageable);
        return page.map(MapperHelpers.getInvoiceMapper()::toInvoiceResult);
    }
}
