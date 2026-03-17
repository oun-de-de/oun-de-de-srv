package com.cdtphuhoi.oun_de_de.services.accounting;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.AccountTypeRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.AccountTypeResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateAccountTypeData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountingService implements OrgManagementService {

    private final AccountTypeRepository accountTypeRepository;

    public AccountTypeResult createAccountType(
        CreateAccountTypeData createAccountTypeData,
        User usr
    ) {
        var accountType = MapperHelpers.getAccountingMapper().toAccountType(createAccountTypeData);
        accountType.setOrgId(usr.getOrgId());
        log.info("Creating account type");
        var accountTypeDb = accountTypeRepository.save(accountType);
        log.info("Created account type, id = {}", accountTypeDb.getId());
        return MapperHelpers.getAccountingMapper().toAccountTypeResult(accountTypeDb);
    }

    public List<AccountTypeResult> findAllAccountTypes() {
        var results = accountTypeRepository.findAll();
        return MapperHelpers.getAccountingMapper().toListAccountTypeResults(results);
    }
}
