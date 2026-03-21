package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.cash_transaction.CreateCashTransactionRequest;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.CashTransactionService;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionFlattenResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionResult;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/cash-transactions")
public class CashTransactionController {

    private final CashTransactionService cashTransactionService;

    @GetMapping
    public ResponseEntity<List<CashTransactionFlattenResult>> listCashTransactions(
        Pageable pageable
    ) {
        return ResponseEntity.ok(cashTransactionService.findBy(pageable));
    }

    @PostMapping
    public ResponseEntity<CashTransactionResult> createCashTransaction(
        @Valid @RequestBody CreateCashTransactionRequest request
    ) {
        var result = cashTransactionService.create(
            MapperHelpers.getCashTransactionMapper().toCreateCashTransactionData(request)
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }
}
