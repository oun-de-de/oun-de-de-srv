package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateAccountTypeRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateChartOfAccountRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateJournalClassRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateJournalTypeRequest;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.accounting.AccountingService;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.AccountTypeResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.ChartOfAccountResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.JournalClassResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.JournalTypeResult;
import com.cdtphuhoi.oun_de_de.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/accounting")
public class AccountingController {

    private final ControllerUtils controllerUtils;

    private final AccountingService accountingService;

    @GetMapping("/chart-of-accounts")
    public ResponseEntity<Page<ChartOfAccountResult>> listChartOfAccounts(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String code,
        @RequestParam(name = "account_type_id", required = false) String accountTypeId,
        @RequestParam(name = "load_account_type", required = false) Boolean shouldLoadAccountType,
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            accountingService.findAllChartOfAccountsBy(
                name,
                code,
                accountTypeId,
                shouldLoadAccountType,
                pageable
            )
        );
    }

    @PostMapping("/chart-of-accounts")
    public ResponseEntity<ChartOfAccountResult> createChartOfAccount(
        @Valid @RequestBody CreateChartOfAccountRequest request
    ) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = accountingService.createChartOfAccount(
            MapperHelpers.getAccountingMapper().toCreateChartOfAccountData(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    @GetMapping("/account-types")
    public ResponseEntity<List<AccountTypeResult>> listAccountTypes() {
        return ResponseEntity.ok(
            accountingService.findAllAccountTypes()
        );
    }

    @PostMapping("/account-types")
    public ResponseEntity<AccountTypeResult> createAccountType(
        @Valid @RequestBody CreateAccountTypeRequest request
    ) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = accountingService.createAccountType(
            MapperHelpers.getAccountingMapper().toCreateAccountTypeData(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    @GetMapping("/journal-classes")
    public ResponseEntity<List<JournalClassResult>> listJournalClasses() {
        return ResponseEntity.ok(
            accountingService.findAllJournalClasses()
        );
    }

    @PostMapping("/journal-classes")
    public ResponseEntity<JournalClassResult> createJournalClass(
        @Valid @RequestBody CreateJournalClassRequest request
    ) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = accountingService.createJournalClass(
            MapperHelpers.getAccountingMapper().toCreateJournalClassData(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    @GetMapping("/journal-types")
    public ResponseEntity<List<JournalTypeResult>> listJournalTypes() {
        return ResponseEntity.ok(
            accountingService.findAllJournalTypes()
        );
    }

    @PostMapping("/journal-types")
    public ResponseEntity<JournalTypeResult> createJournalType(
        @Valid @RequestBody CreateJournalTypeRequest request
    ) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = accountingService.createJournalType(
            MapperHelpers.getAccountingMapper().toCreateJournalTypeData(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }
}
