package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.accounting.CreateAccountTypeRequest;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.accounting.AccountingService;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.AccountTypeResult;
import com.cdtphuhoi.oun_de_de.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/accounting")
public class AccountingController {

    private final ControllerUtils controllerUtils;

    private final AccountingService accountingService;

    @GetMapping("/account-types")
    public ResponseEntity<List<AccountTypeResult>> listAccountTypes() {
        return ResponseEntity.ok(
            accountingService.findAllAccountTypes()
        );
    }

    @PostMapping("/account-types")
    public ResponseEntity<AccountTypeResult> createAccountType(
        @Valid @RequestBody CreateAccountTypeRequest request) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = accountingService.createAccountType(
            MapperHelpers.getAccountingMapper().toCreateAccountTypeData(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }
}
