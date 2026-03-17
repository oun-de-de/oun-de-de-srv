package com.cdtphuhoi.oun_de_de.services.accounting;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.AccountTypeRepository;
import com.cdtphuhoi.oun_de_de.repositories.ChartOfAccountRepository;
import com.cdtphuhoi.oun_de_de.repositories.JournalClassRepository;
import com.cdtphuhoi.oun_de_de.repositories.JournalTypeRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.AccountTypeResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.ChartOfAccountResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateAccountTypeData;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateChartOfAccountData;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateJournalClassData;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.CreateJournalTypeData;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.JournalClassResult;
import com.cdtphuhoi.oun_de_de.services.accounting.dto.JournalTypeResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountingService implements OrgManagementService {

    private final AccountTypeRepository accountTypeRepository;

    private final JournalClassRepository journalClassRepository;

    private final JournalTypeRepository journalTypeRepository;

    private final ChartOfAccountRepository chartOfAccountRepository;

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

    public JournalClassResult createJournalClass(
        CreateJournalClassData createJournalClassData,
        User usr
    ) {
        var journalClass = MapperHelpers.getAccountingMapper().toJournalClass(createJournalClassData);
        journalClass.setOrgId(usr.getOrgId());
        log.info("Creating journal class");
        var journalClassDb = journalClassRepository.save(journalClass);
        log.info("Created journal class, id = {}", journalClassDb.getId());
        return MapperHelpers.getAccountingMapper().toJournalClassResult(journalClassDb);
    }


    public List<JournalClassResult> findAllJournalClasses() {
        var results = journalClassRepository.findAll();
        return MapperHelpers.getAccountingMapper().toListJournalClassResults(results);
    }

    public List<JournalTypeResult> findAllJournalTypes() {
        var results = journalTypeRepository.findAll();
        return MapperHelpers.getAccountingMapper().toListJournalTypeResults(results);
    }

    public JournalTypeResult createJournalType(
        CreateJournalTypeData createJournalTypeData,
        User usr
    ) {
        var journalType = MapperHelpers.getAccountingMapper().toJournalType(createJournalTypeData);
        journalType.setOrgId(usr.getOrgId());
        log.info("Creating journal type");
        var journalTypeDb = journalTypeRepository.save(journalType);
        log.info("Created journal type, id = {}", journalTypeDb.getId());
        return MapperHelpers.getAccountingMapper().toJournalTypeResult(journalTypeDb);
    }

    public ChartOfAccountResult createChartOfAccount(
        CreateChartOfAccountData createChartOfAccountData,
        User usr
    ) {
        var accountType = accountTypeRepository.findOneById(createChartOfAccountData.getAccountTypeId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("AccountType [id=%s] not found", createChartOfAccountData.getAccountTypeId())
                )
            );
        var chartOfAccount = MapperHelpers.getAccountingMapper().toChartOfAccount(createChartOfAccountData);
        chartOfAccount.setAccountType(accountType);
        chartOfAccount.setOrgId(usr.getOrgId());
        log.info("Creating chart of account");
        var chartOfAccountDb = chartOfAccountRepository.save(chartOfAccount);
        log.info("Created chart of account, id = {}", chartOfAccountDb.getId());
        return MapperHelpers.getAccountingMapper().toChartOfAccountResult(chartOfAccountDb);
    }

    public Page<ChartOfAccountResult> findAllChartOfAccountsBy(
        String name,
        String code,
        String accountTypeId,
        Pageable pageable
    ) {
        var page = chartOfAccountRepository.findAll(
            Specification.allOf(
                ChartOfAccountSpecifications.hasCode(code),
                ChartOfAccountSpecifications.containName(name),
                ChartOfAccountSpecifications.hasAccountTypeId(accountTypeId)
            ),
            pageable
        );
        return page.map(MapperHelpers.getAccountingMapper()::toChartOfAccountResult);
    }
}
