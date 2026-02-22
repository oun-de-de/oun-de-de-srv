package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.services.loan.LoanService;
import com.cdtphuhoi.oun_de_de.services.loan.dto.LoanResult;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

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


}
