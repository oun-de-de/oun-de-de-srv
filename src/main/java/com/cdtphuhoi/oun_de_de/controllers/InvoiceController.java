package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.common.InvoiceType;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.controllers.dto.invoice.ExportInvoicesRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.invoice.UpdateInvoicesRequest;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.invoice.InvoiceService;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceExportLineResult;
import com.cdtphuhoi.oun_de_de.services.invoice.dto.InvoiceResult;
import com.cdtphuhoi.oun_de_de.services.payment.PaymentTermService;
import com.cdtphuhoi.oun_de_de.services.payment.dto.PaymentTermCycleResult;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1")
public class InvoiceController {

    private final InvoiceService invoiceService;

    private final PaymentTermService paymentTermService;

    @GetMapping("/cycles")
    public ResponseEntity<Page<PaymentTermCycleResult>> listCycles(
        @RequestParam(
            name = "customer_id",
            required = false
        ) String customerId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam(
            name = "status",
            required = false
        ) PaymentTermCycleStatus status,
        @RequestParam(required = false) Integer duration,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            paymentTermService.findPaymentTermCyclesBy(
                customerId,
                from,
                to,
                duration,
                status,
                pageable
            )
        );
    }

    @GetMapping("/invoices")
    public ResponseEntity<Page<InvoiceResult>> listInvoices(
        @RequestParam(
            name = "cycle_id",
            required = false
        ) String cycleId,
        @RequestParam(
            name = "customer_id",
            required = false
        ) String customerId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam(
            name = "type",
            required = false
        ) InvoiceType invoiceType,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            invoiceService.findBy(
                cycleId,
                customerId,
                from,
                to,
                invoiceType,
                pageable
            )
        );
    }

    @PostMapping("/invoices/export")
    public ResponseEntity<List<InvoiceExportLineResult>> listInvoiceDetails(
        @Valid @RequestBody ExportInvoicesRequest request
    ) {
        var result = invoiceService.queryForExport(
            MapperHelpers.getInvoiceMapper().toExportInvoicesRequestData(request)
        );
        return ResponseEntity.ok(result);
    }

    @PutMapping("/invoices/update-batch")
    public ResponseEntity<String> updateInvoices(
        @Valid @RequestBody UpdateInvoicesRequest request
    ) {
        invoiceService.updateInvoices(MapperHelpers.getInvoiceMapper().toUpdateInvoicesData(request));
        return ResponseEntity.ok("Update successfully");
    }
}
