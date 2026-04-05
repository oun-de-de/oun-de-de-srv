package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.common.dto.CodeResponse;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.CreateLoanRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.CreateLoanPaymentRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.ExtendLoanRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.loan.UpdateLoanRequest;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import com.cdtphuhoi.oun_de_de.services.loan.LoanService;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanPaymentResult;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import com.cdtphuhoi.oun_de_de.utils.SecurityContextUtils;
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

    @PutMapping("/{loanId}")
    public ResponseEntity<LoanResult> updateLoan(
        @PathVariable String loanId,
        @Valid @RequestBody UpdateLoanRequest request
    ) {
        return ResponseEntity.ok(
            loanService.updateLoan(
                loanId,
                MapperHelpers.getLoanMapper().toUpdateLoanData(request)
            )
        );
    }

    @PostMapping("/{loanId}/postpone")
    public ResponseEntity<LoanResult> postponeLoan(@PathVariable String loanId) {
        return ResponseEntity.ok(loanService.postponeLoan(loanId));
    }

    @PostMapping("/{loanId}/extend-loan")
    public ResponseEntity<LoanResult> extendLoan(
        @PathVariable String loanId,
        @Valid @RequestBody ExtendLoanRequest request
    ) {
        return ResponseEntity.ok(
            loanService.extendLoan(
                loanId,
                MapperHelpers.getLoanMapper().toExtendLoanData(request)
            )
        );
    }

    @GetMapping("/{loanId}/payments")
    public ResponseEntity<List<LoanPaymentResult>> listLoanPayments(
        @PathVariable String loanId
    ) {
        return ResponseEntity.ok(loanService.findLoanPaymentsByLoanId(loanId));
    }

    @PostMapping("/{loanId}/payments")
    public ResponseEntity<LoanPaymentResult> createPayment(
        @PathVariable String loanId,
        @Valid @RequestBody CreateLoanPaymentRequest request
    ) {
        return ResponseEntity.ok(
            loanService.createPayment(
                loanId,
                MapperHelpers.getLoanMapper().toCreateLoanPaymentData(request)
            )
        );
    }

    @GetMapping("/generate-payment-code")
    public ResponseEntity<CodeResponse> generatePaymentCode() {
        var orgId = SecurityContextUtils.getCurrentUserProperty(UserDetailsImpl::getOrgId);
        return ResponseEntity.ok(
            loanService.generatePaymentCode(orgId)
        );
    }
}
