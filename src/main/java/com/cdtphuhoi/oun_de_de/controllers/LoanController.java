package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.CreateLoanRequest;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.loan.LoanService;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanInstallmentResult;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<Page<LoanResult>> listLoans(
        @RequestParam(
            name = "borrower_type",
            required = false
        ) BorrowerType borrowerType,
        @RequestParam(
            name = "borrower_id",
            required = false
        ) String borrowerId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            loanService.findBy(
                borrowerType,
                borrowerId,
                from,
                to,
                pageable
            )
        );
    }

    @PostMapping
    public ResponseEntity<LoanResult> createLoan(
        @Valid @RequestBody CreateLoanRequest request
    ) {
        var result = loanService.createLoan(MapperHelpers.getLoanMapper().toCreateLoanData(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResult> getLoanDetails(@PathVariable String loanId) {
        return ResponseEntity.ok(loanService.findLoanById(loanId));
    }

    @GetMapping("/{loanId}/installments")
    public ResponseEntity<List<LoanInstallmentResult>> listLoanInstallments(
        @PathVariable String loanId
    ) {
        return ResponseEntity.ok(loanService.findLoanInstallmentsByLoanId(loanId));
    }

    @PostMapping("/{loanId}/installments/{installmentId}/pay")
    public ResponseEntity<LoanInstallmentResult> payInstallment(
        @PathVariable String loanId,
        @PathVariable String installmentId
    ) {
        return ResponseEntity.ok(
            loanService.payInstallment(loanId, installmentId)
        );
    }
}
