package com.cdtphuhoi.oun_de_de.services.cash_transaction;

import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import com.cdtphuhoi.oun_de_de.entities.AccountType;
import com.cdtphuhoi.oun_de_de.entities.CashTransaction;
import com.cdtphuhoi.oun_de_de.entities.CashTransactionDetail;
import com.cdtphuhoi.oun_de_de.entities.CashTransaction_;
import com.cdtphuhoi.oun_de_de.entities.ChartOfAccount;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.JournalClass;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.AccountTypeRepository;
import com.cdtphuhoi.oun_de_de.repositories.CashTransactionRepository;
import com.cdtphuhoi.oun_de_de.repositories.ChartOfAccountRepository;
import com.cdtphuhoi.oun_de_de.repositories.CurrencyRepository;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.JournalClassRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionFlattenResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CashTransactionResult;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CreateCashTransactionData;
import com.cdtphuhoi.oun_de_de.services.cash_transaction.dto.CreateCashTransactionDetailData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.JoinType;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CashTransactionService implements OrgManagementService {

    private final UserRepository userRepository;

    private final CurrencyRepository currencyRepository;

    private final ChartOfAccountRepository chartOfAccountRepository;

    private final AccountTypeRepository accountTypeRepository;

    private final CustomerRepository customerRepository;

    private final JournalClassRepository journalClassRepository;

    private final CashTransactionRepository cashTransactionRepository;

    public CashTransactionResult create(CreateCashTransactionData createCashTransactionData) {
        var invalidAccountType = CashTransactionType.CREDIT.equals(createCashTransactionData.getType()) &&
            createCashTransactionData.getCashTransactionDetails().stream()
                .anyMatch(detail -> detail.getAccountTypeId() == null);
        if (invalidAccountType) {
            throw new BadRequestException("Account Type is required for Credit Cash Transaction");
        }
        var employee = userRepository.findOneById(createCashTransactionData.getEmployeeId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Employee [id=%s] not found", createCashTransactionData.getEmployeeId())
                )
            );
        var currency = Optional.ofNullable(createCashTransactionData.getCurrencyId())
            .map(currencyId -> currencyRepository.findOneById(currencyId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Currency [id=%s] not found", currencyId)
                    )
                )
            )
            .orElse(null);
        var cashTransaction = CashTransaction.builder()
            .orgId(employee.getOrgId())
            .refNo(createCashTransactionData.getRefNo())
            .type(createCashTransactionData.getType())
            .date(createCashTransactionData.getDate())
            .currency(currency)
            .employee(employee)
            .memo(createCashTransactionData.getMemo())
            .build();
        var cashTransactionDetails = createCashTransactionDetailsInMem(
            createCashTransactionData.getCashTransactionDetails(),
            cashTransaction
        );
        cashTransaction.setCashTransactionDetails(cashTransactionDetails);
        log.info("Creating cash transaction with refNo = {}", cashTransaction.getRefNo());
        var cashTransactionDb = cashTransactionRepository.save(cashTransaction);
        log.info("Created cash transaction with id = {}", cashTransactionDb.getId());
        return MapperHelpers.getCashTransactionMapper().toCashTransactionResult(cashTransactionDb);
    }

    private List<CashTransactionDetail> createCashTransactionDetailsInMem(
        List<CreateCashTransactionDetailData> cashTransactionDetails,
        CashTransaction cashTransaction
    ) {
        var chartOfAccountById = buildChartOfAccountById(cashTransactionDetails);
        var accountTypeById = buildAccountTypeById(cashTransactionDetails);
        var customerById = buildCustomerById(cashTransactionDetails);
        var journalClassById = buildJournalClassById(cashTransactionDetails);
        return cashTransactionDetails.stream()
            .map(request ->
                CashTransactionDetail.builder()
                    .orgId(cashTransaction.getOrgId())
                    .cashTransaction(cashTransaction)
                    .chartOfAccount(chartOfAccountById.get(request.getChartOfAccountId()))
                    .accountType(
                        Optional.ofNullable(request.getAccountTypeId())
                            .map(accountTypeById::get)
                            .orElse(null)
                    )
                    .memo(request.getMemo())
                    .amount(request.getAmount())
                    .customer(customerById.get(request.getCustomerId()))
                    .journalClass(
                        Optional.ofNullable(request.getJournalClassId())
                            .map(journalClassById::get)
                            .orElse(null)
                    )
                    .build()
            )
            .collect(Collectors.toList());
    }

    private Map<String, JournalClass> buildJournalClassById(List<CreateCashTransactionDetailData> cashTransactionDetails) {
        var journalClassIds = cashTransactionDetails.stream()
            .map(CreateCashTransactionDetailData::getJournalClassId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        var journalClassMap = journalClassRepository.findAllByIdIn(journalClassIds).stream()
            .collect(
                Collectors.toMap(
                    JournalClass::getId,
                    Function.identity()
                )
            );
        if (journalClassMap.size() != journalClassIds.size()) {
            throw new BadRequestException(
                String.format(
                    "Some Journal Class not found. Expected: %d, Actual: %d",
                    journalClassIds.size(),
                    journalClassMap.size()
                )
            );
        }
        return journalClassMap;
    }

    private Map<String, Customer> buildCustomerById(List<CreateCashTransactionDetailData> cashTransactionDetails) {
        var customerIds = cashTransactionDetails.stream()
            .map(CreateCashTransactionDetailData::getCustomerId)
            .collect(Collectors.toSet());
        var customerMap = customerRepository.findAllByIdIn(customerIds).stream()
            .collect(
                Collectors.toMap(
                    Customer::getId,
                    Function.identity()
                )
            );
        if (customerMap.size() != customerIds.size()) {
            throw new BadRequestException(
                String.format(
                    "Some Customer not found. Expected: %d, Actual: %d",
                    customerIds.size(),
                    customerMap.size()
                )
            );
        }
        return customerMap;
    }

    private Map<String, AccountType> buildAccountTypeById(List<CreateCashTransactionDetailData> cashTransactionDetails) {
        var accountTypeIds = cashTransactionDetails.stream()
            .map(CreateCashTransactionDetailData::getAccountTypeId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        var accountTypeMap = accountTypeRepository.findAllByIdIn(accountTypeIds).stream()
            .collect(
                Collectors.toMap(
                    AccountType::getId,
                    Function.identity()
                )
            );
        if (accountTypeMap.size() != accountTypeIds.size()) {
            throw new BadRequestException(
                String.format(
                    "Some Account Type not found. Expected: %d, Actual: %d",
                    accountTypeIds.size(),
                    accountTypeMap.size()
                )
            );
        }
        return accountTypeMap;
    }

    private Map<String, ChartOfAccount> buildChartOfAccountById
        (List<CreateCashTransactionDetailData> cashTransactionDetails) {
        var chartOfAccountIds = cashTransactionDetails.stream()
            .map(CreateCashTransactionDetailData::getChartOfAccountId)
            .collect(Collectors.toSet());
        var chartOfAccountMap = chartOfAccountRepository.findAllByIdIn(chartOfAccountIds).stream()
            .collect(
                Collectors.toMap(
                    ChartOfAccount::getId,
                    Function.identity()
                )
            );
        if (chartOfAccountMap.size() != chartOfAccountIds.size()) {
            throw new BadRequestException(
                String.format(
                    "Some Chart of Account not found. Expected: %d, Actual: %d",
                    chartOfAccountIds.size(),
                    chartOfAccountMap.size()
                )
            );
        }
        return chartOfAccountMap;
    }

    public List<CashTransactionFlattenResult> findBy(Pageable pageable) {
        var page = cashTransactionRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> {
                    if (query != null && Long.class != query.getResultType()) {
                        root.fetch(CashTransaction_.CURRENCY, JoinType.LEFT);
                        root.fetch(CashTransaction_.CASH_TRANSACTION_DETAILS, JoinType.LEFT);
                    }
                    return null;
                }
            ),
            pageable
        );
        var cashTransactions = page.getContent();
        return MapperHelpers.getCashTransactionMapper().toListCashTransactionFlattenResults(cashTransactions);
    }
}
