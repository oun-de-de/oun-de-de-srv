package com.cdtphuhoi.oun_de_de.services.reports;

import static com.cdtphuhoi.oun_de_de.utils.Utils.endOfDayInCambodia;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import com.cdtphuhoi.oun_de_de.common.CashTransactionReason;
import com.cdtphuhoi.oun_de_de.common.CashTransactionType;
import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.entities.AccountType_;
import com.cdtphuhoi.oun_de_de.entities.CashTransaction;
import com.cdtphuhoi.oun_de_de.entities.CashTransactionDetail;
import com.cdtphuhoi.oun_de_de.entities.CashTransactionDetail_;
import com.cdtphuhoi.oun_de_de.entities.CashTransaction_;
import com.cdtphuhoi.oun_de_de.entities.Customer_;
import com.cdtphuhoi.oun_de_de.entities.Invoice;
import com.cdtphuhoi.oun_de_de.entities.Invoice_;
import com.cdtphuhoi.oun_de_de.entities.LoanPayment;
import com.cdtphuhoi.oun_de_de.entities.LoanPayment_;
import com.cdtphuhoi.oun_de_de.entities.MonthlyBalance;
import com.cdtphuhoi.oun_de_de.entities.Payment;
import com.cdtphuhoi.oun_de_de.entities.Payment_;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction_;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord_;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle_;
import com.cdtphuhoi.oun_de_de.entities.ChartOfAccount_;
import com.cdtphuhoi.oun_de_de.entities.JournalClass_;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CashTransactionRepository;
import com.cdtphuhoi.oun_de_de.repositories.MonthlyBalanceRepository;
import com.cdtphuhoi.oun_de_de.repositories.StockTransactionRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.reports.dto.CashTransactionDetailProjection;
import com.cdtphuhoi.oun_de_de.services.reports.dto.CashTransactionReportLine;
import com.cdtphuhoi.oun_de_de.services.reports.dto.CashTransactionReportResponse;
import com.cdtphuhoi.oun_de_de.services.reports.dto.OpenInvoiceCycleGroup;
import com.cdtphuhoi.oun_de_de.services.reports.dto.OpenInvoiceCustomerGroup;
import com.cdtphuhoi.oun_de_de.services.reports.dto.OpenInvoiceProjection;
import com.cdtphuhoi.oun_de_de.services.reports.dto.OpenInvoiceReportLine;
import com.cdtphuhoi.oun_de_de.services.reports.dto.DailyReportResponse;
import com.cdtphuhoi.oun_de_de.services.reports.dto.InventoryStockReportLine;
import com.cdtphuhoi.oun_de_de.services.reports.dto.MonthlyCashTransactionDetail;
import com.cdtphuhoi.oun_de_de.services.reports.dto.MonthlyExpenseLine;
import com.cdtphuhoi.oun_de_de.services.reports.dto.MonthlyReportDetailsResponse;
import com.cdtphuhoi.oun_de_de.services.reports.dto.MonthlyReportLine;
import com.cdtphuhoi.oun_de_de.services.reports.dto.MonthlyReportResponse;
import com.cdtphuhoi.oun_de_de.services.reports.dto.ProductRevenue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReportingService implements OrgManagementService {

    private final StockTransactionRepository stockTransactionRepository;

    private final CashTransactionRepository cashTransactionRepository;

    private final MonthlyBalanceRepository monthlyBalanceRepository;

    private final EntityManager entityManager;

    public DailyReportResponse getDailyReport(LocalDate date) {
        var soldProducts = getSoldProductsByDate(date);
        var totalRevenue = soldProducts.stream()
            .map(ProductRevenue::totalAmount)
            .filter(Objects::nonNull)
            .reduce(
                BigDecimal.ZERO,
                BigDecimal::add
            );

        var boughtItems = stockTransactionRepository.findAll(
            Specification.allOf(
                (root, query, cb) ->
                    cb.equal(
                        root.get(StockTransaction_.REASON),
                        StockTransactionReason.PURCHASE
                    ),
                (root, query, cb) -> buildDatePredicate(date, cb, root.get(StockTransaction_.CREATED_AT)),
                (root, query, cb) -> {
                    root.fetch(StockTransaction_.ITEM, JoinType.LEFT);
                    return null;
                }
            )
        );

        var totalExpense = boughtItems.stream()
            .map(StockTransaction::getExpense)
            .filter(Objects::nonNull)
            .reduce(
                BigDecimal.ZERO,
                BigDecimal::add
            );

        var totalCashReceive = calculateTotalCashReceive(date);

        return DailyReportResponse.builder()
            .soldProducts(soldProducts)
            .boughtItems(MapperHelpers.getReportMapper().toListDailyBoughtItems(boughtItems))
            .totalRevenue(totalRevenue)
            .totalCashReceive(totalCashReceive)
            .totalExpense(totalExpense)
            .build();
    }

    private List<ProductRevenue> getSoldProductsByDate(LocalDate date) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(ProductRevenue.class);
        var root = query.from(WeightRecord.class);
        query
            .select(
                cb.construct(
                    ProductRevenue.class,
                    root.get(WeightRecord_.PRODUCT_NAME),
                    root.get(WeightRecord_.UNIT),
                    cb.coalesce(
                        cb.sum(root.get(WeightRecord_.QUANTITY)),
                        BigDecimal.ZERO
                    ),
                    cb.coalesce(
                        cb.sum(root.get(WeightRecord_.AMOUNT)),
                        BigDecimal.ZERO
                    )
                )
            )
            .where(
                root.get(WeightRecord_.PRODUCT_NAME).isNotNull(),
                root.get(WeightRecord_.PRICE_PER_PRODUCT).isNotNull(),
                root.get(WeightRecord_.QUANTITY).isNotNull(),
                buildDatePredicate(date, cb, root.get(WeightRecord_.INVOICE).get(Invoice_.DATE))
            )
            .groupBy(
                root.get(WeightRecord_.PRODUCT_NAME),
                root.get(WeightRecord_.UNIT)
            );

        return entityManager.createQuery(query).getResultList();
    }

    private BigDecimal calculateTotalCashReceive(LocalDate date) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(BigDecimal.class);
        var root = query.from(Payment.class);
        query
            .select(
                cb.coalesce(
                    cb.sum(root.get(Payment_.AMOUNT)),
                    BigDecimal.ZERO
                )
            )
            .where(
                buildDatePredicate(date, cb, root.get(Payment_.PAYMENT_DATE))
            );
        return entityManager.createQuery(query).getSingleResult();
    }

    private static Predicate buildDatePredicate(LocalDate date, CriteriaBuilder cb, Path<?> path) {
        return cb.equal(
            cb.function(
                "DATE",
                LocalDate.class,
                path
            ),
            date
        );
    }

    public List<InventoryStockReportLine> getInventoryStockReport(LocalDate fromDate, LocalDate toDate) {
        var transactions = stockTransactionRepository.findAll(
            Specification.allOf(
                (root, query, cb) ->
                    cb.between(
                        cb.function(
                            "DATE",
                            LocalDate.class,
                            root.get(StockTransaction_.createdAt)
                        ),
                        fromDate,
                        toDate
                    ),
                (root, query, cb) -> {
                    root.fetch(StockTransaction_.ITEM, JoinType.LEFT);
                    return null;
                }
            )
        );
        return MapperHelpers.getReportMapper().toListInventoryStockReportLines(transactions);
    }

    public MonthlyReportResponse getMonthlyReport(YearMonth yearMonth) {
        var start = startOfDayInCambodia(yearMonth.atDay(1).atStartOfDay());
        var end = endOfDayInCambodia(yearMonth.atEndOfMonth().atTime(LocalTime.MAX));
        var accountsReceivable = calculateCashReceivableBetween(start, end);
        var saleInvoice = calculateSaleInvoiceBetween(start, end);
        var cashInstallment = calculateCashInstallmentBetween(start, end);
        var monthlyExpensesDetails = getMonthlyExpensesDetails(start, end);

        return MonthlyReportResponse.builder()
            .accountsReceivable(accountsReceivable)
            .saleInvoice(saleInvoice)
            .cashInstallment(cashInstallment)
            .expenses(monthlyExpensesDetails)
            .build();
    }

    private List<MonthlyExpenseLine> getMonthlyExpensesDetails(LocalDateTime start, LocalDateTime end) {
        var expenseCashTransactions = cashTransactionRepository.findAll(
            Specification.allOf(
                (root, query, cb) ->
                    cb.between(
                        root.get(CashTransaction_.date),
                        start,
                        end
                    ),
                (root, query, cb) ->
                    cb.equal(
                        root.get(CashTransaction_.TYPE),
                        CashTransactionType.CREDIT
                    ),
                (root, query, cb) -> {
                    root.fetch(CashTransaction_.CASH_TRANSACTION_DETAILS, JoinType.LEFT);
                    return null;
                }
            )
        );
        var cashTransactionDetails = expenseCashTransactions.stream()
            .map(CashTransaction::getCashTransactionDetails)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .toList();
        return cashTransactionDetails.stream()
            .map(cashTransactionDetail ->
                MonthlyExpenseLine.builder()
                    .description(cashTransactionDetail.getMemo())
                    .amount(cashTransactionDetail.getAmount())
                    .build()
            )
            .toList();
    }

    private BigDecimal calculateCashInstallmentBetween(LocalDateTime start, LocalDateTime end) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(BigDecimal.class);
        var root = query.from(LoanPayment.class);
        query
            .select(
                cb.coalesce(
                    cb.sum(root.get(LoanPayment_.AMOUNT)),
                    BigDecimal.ZERO
                )
            )
            .where(
                cb.between(
                    root.get(LoanPayment_.paymentDate),
                    start,
                    end
                )
            );
        return entityManager.createQuery(query).getSingleResult();
    }

    private BigDecimal calculateSaleInvoiceBetween(LocalDateTime start, LocalDateTime end) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(BigDecimal.class);
        var root = query.from(Payment.class);
        query
            .select(
                cb.coalesce(
                    cb.sum(root.get(Payment_.AMOUNT)),
                    BigDecimal.ZERO
                )
            )
            .where(
                cb.between(
                    root.get(Payment_.paymentDate),
                    start,
                    end
                )
            );
        return entityManager.createQuery(query).getSingleResult();
    }

    private BigDecimal calculateCashReceivableBetween(LocalDateTime start, LocalDateTime end) {
        var invoiceIds = getInvoiceIdsByDateRange(start, end);
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(BigDecimal.class);
        var root = query.from(WeightRecord.class);
        query
            .select(
                cb.coalesce(
                    cb.sum(root.get(WeightRecord_.AMOUNT)),
                    BigDecimal.ZERO
                )
            )
            .where(
                root.get(WeightRecord_.INVOICE).get(Invoice_.ID).in(invoiceIds)
            );
        return entityManager.createQuery(query).getSingleResult();
    }

    private List<String> getInvoiceIdsByDateRange(LocalDateTime start, LocalDateTime end) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(String.class);
        var root = query.from(Invoice.class);
        query
            .select(
                root.get(Invoice_.ID)
            )
            .where(
                cb.between(
                    root.get(Invoice_.date),
                    start,
                    end
                )
            );
        return entityManager.createQuery(query).getResultList();
    }


    public MonthlyReportDetailsResponse getMonthlyReportDetails(YearMonth yearMonth) {
        var start = startOfDayInCambodia(yearMonth.atDay(1).atStartOfDay());
        var end = endOfDayInCambodia(yearMonth.atEndOfMonth().atTime(LocalTime.MAX));
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(MonthlyCashTransactionDetail.class);
        var root = query.from(CashTransactionDetail.class);
        var cashTxJoin = root.join(CashTransactionDetail_.cashTransaction, JoinType.LEFT);
        var accTypeJoin = root.join(CashTransactionDetail_.accountType, JoinType.LEFT);
        var customerJoin = root.join(CashTransactionDetail_.customer, JoinType.LEFT);
        query
            .select(
                cb.construct(
                    MonthlyCashTransactionDetail.class,
                    cashTxJoin.get(CashTransaction_.DATE),
                    cashTxJoin.get(CashTransaction_.REF_NO),
                    cashTxJoin.get(CashTransaction_.TYPE),
                    cashTxJoin.get(CashTransaction_.REASON),
                    accTypeJoin.get(AccountType_.NATURE),
                    customerJoin.get(Customer_.NAME),
                    root.get(CashTransactionDetail_.MEMO),
                    root.get(CashTransactionDetail_.AMOUNT)
                )
            )
            .where(
                cb.between(
                    cashTxJoin.get(CashTransaction_.date),
                    start,
                    end
                )
            )
            .orderBy(
                cb.desc(cashTxJoin.get(CashTransaction_.DATE))
            );
        var cashTransactionDetails = entityManager.createQuery(query).getResultList();

        var previousBalance = monthlyBalanceRepository.findByPeriod(yearMonth.minusMonths(1).toString())
            .map(MonthlyBalance::getClosingBalance)
            .orElse(BigDecimal.ZERO);
        var balance = new AtomicReference<>(previousBalance);
        var monthlyReportLines = cashTransactionDetails.stream()
            .map(detail -> {
                    var isDebit = CashTransactionType.DEBIT.equals(detail.type());
                    return MonthlyReportLine.builder()
                        .date(detail.date())
                        .refNo(detail.refNo())
                        .reason(getReason(detail))
                        .customerName(detail.customerName())
                        .memo(detail.memo())
                        .debit(isDebit ? detail.amount() : null)
                        .credit(!isDebit ? detail.amount() : null)
                        .balance(isDebit ?
                            balance.updateAndGet(b -> b.add(detail.amount())) :
                            balance.updateAndGet(b -> b.subtract(detail.amount()))
                        )
                        .build();
                }
            )
            .toList();
        return MonthlyReportDetailsResponse.builder()
            .lines(monthlyReportLines)
            .initCashOnHand(previousBalance)
            .build();
    }

    private static String getReason(MonthlyCashTransactionDetail cashTransactionDetail) {
        if (cashTransactionDetail.accountNature() != null) {
            return cashTransactionDetail.accountNature();
        }
        if (cashTransactionDetail.reason() != null) {
            return cashTransactionDetail.reason().toString();
        }
        return CashTransactionType.DEBIT.equals(cashTransactionDetail.type()) ?
            CashTransactionReason.CASH_IN.toString() :
            CashTransactionReason.CASH_OUT.toString();
    }

    public List<OpenInvoiceCustomerGroup> getOpenInvoiceReport(
        LocalDate from,
        LocalDate to,
        String customerId
    ) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(OpenInvoiceProjection.class);
        var root = query.from(Invoice.class);
        var cycleJoin = root.join(Invoice_.CYCLE, JoinType.LEFT);

        var subquery = query.subquery(BigDecimal.class);
        var wrRoot = subquery.from(WeightRecord.class);
        subquery.select(
            cb.coalesce(cb.sum(wrRoot.get(WeightRecord_.AMOUNT)), BigDecimal.ZERO)
        ).where(
            cb.equal(wrRoot.get(WeightRecord_.INVOICE), root)
        );

        query.select(cb.construct(
            OpenInvoiceProjection.class,
            root.get(Invoice_.DATE),
            root.get(Invoice_.REF_NO),
            root.get(Invoice_.CUSTOMER_NAME),
            cycleJoin.get(PaymentTermCycle_.ID),
            cycleJoin.get(PaymentTermCycle_.START_DATE),
            cycleJoin.get(PaymentTermCycle_.END_DATE),
            cycleJoin.get(PaymentTermCycle_.TOTAL_PAID_AMOUNT),
            subquery
        ));

        var predicates = new ArrayList<Predicate>();
        predicates.add(cb.greaterThanOrEqualTo(root.get(Invoice_.DATE), from.atStartOfDay()));
        predicates.add(cb.lessThanOrEqualTo(root.get(Invoice_.DATE), to.atTime(LocalTime.MAX)));
        if (customerId != null) {
            predicates.add(cb.equal(root.get(Invoice_.CUSTOMER).get(Customer_.ID), customerId));
        }
        query.where(predicates.toArray(new Predicate[0]));

        query.orderBy(
            cb.asc(root.get(Invoice_.CUSTOMER_NAME)),
            cb.asc(cycleJoin.get(PaymentTermCycle_.START_DATE)),
            cb.asc(root.get(Invoice_.DATE))
        );

        var rows = entityManager.createQuery(query).getResultList();

        // customer → cycleId → lines
        Map<String, Map<String, List<OpenInvoiceReportLine>>> customerCycleMap = new LinkedHashMap<>();
        // keep cycle metadata keyed by cycleId
        Map<String, OpenInvoiceProjection> cycleMetaMap = new LinkedHashMap<>();

        for (var row : rows) {
            customerCycleMap
                .computeIfAbsent(row.customerName(), k -> new LinkedHashMap<>())
                .computeIfAbsent(row.cycleId(), k -> new ArrayList<>())
                .add(OpenInvoiceReportLine.builder()
                    .date(row.invoiceDate())
                    .refNo(row.refNo())
                    .originalAmount(row.originalAmount())
                    .build());
            cycleMetaMap.putIfAbsent(row.cycleId(), row);
        }

        return customerCycleMap.entrySet().stream()
            .map(customerEntry -> {
                var cycles = customerEntry.getValue().entrySet().stream()
                    .map(cycleEntry -> {
                        var meta = cycleMetaMap.get(cycleEntry.getKey());
                        var invoiceLines = cycleEntry.getValue();
                        var totalOriginal = invoiceLines.stream()
                            .map(OpenInvoiceReportLine::getOriginalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                        return OpenInvoiceCycleGroup.builder()
                            .cycleStartDate(meta.cycleStartDate())
                            .cycleEndDate(meta.cycleEndDate())
                            .totalOriginalAmount(totalOriginal)
                            .totalPaidAmount(meta.totalPaidAmount())
                            .balance(totalOriginal.subtract(meta.totalPaidAmount()))
                            .invoices(invoiceLines)
                            .build();
                    })
                    .toList();
                return OpenInvoiceCustomerGroup.builder()
                    .customerName(customerEntry.getKey())
                    .cycles(cycles)
                    .build();
            })
            .toList();
    }

    public CashTransactionReportResponse getCashTransactionReport(
        String journalClassId,
        String chartOfAccountId,
        LocalDate from,
        LocalDate to
    ) {
        var cb = entityManager.getCriteriaBuilder();
        var query = cb.createQuery(CashTransactionDetailProjection.class);
        var root = query.from(CashTransactionDetail.class);
        var cashTxJoin = root.join(CashTransactionDetail_.cashTransaction, JoinType.LEFT);
        var journalClassJoin = root.join(CashTransactionDetail_.journalClass, JoinType.LEFT);

        query.select(
            cb.construct(
                CashTransactionDetailProjection.class,
                cashTxJoin.get(CashTransaction_.DATE),
                cashTxJoin.get(CashTransaction_.REF_NO),
                cashTxJoin.get(CashTransaction_.TYPE),
                journalClassJoin.get(JournalClass_.NAME),
                root.get(CashTransactionDetail_.MEMO),
                root.get(CashTransactionDetail_.AMOUNT)
            )
        );

        var predicates = new ArrayList<Predicate>();

        if (journalClassId != null) {
            predicates.add(
                cb.equal(journalClassJoin.get(JournalClass_.ID), journalClassId)
            );
        }
        if (chartOfAccountId != null) {
            var coaJoin = root.join(CashTransactionDetail_.chartOfAccount, JoinType.LEFT);
            predicates.add(
                cb.equal(coaJoin.get(ChartOfAccount_.ID), chartOfAccountId)
            );
        }
        predicates.add(
            cb.greaterThanOrEqualTo(
                cb.function("DATE", LocalDate.class, cashTxJoin.get(CashTransaction_.DATE)),
                from
            )
        );
        predicates.add(
            cb.lessThanOrEqualTo(
                cb.function("DATE", LocalDate.class, cashTxJoin.get(CashTransaction_.DATE)),
                to
            )
        );

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(cashTxJoin.get(CashTransaction_.DATE)));

        var rows = entityManager.createQuery(query).getResultList();

        var previousBalance = monthlyBalanceRepository
            .findByPeriod(YearMonth.from(from).minusMonths(1).toString())
            .map(MonthlyBalance::getClosingBalance)
            .orElse(BigDecimal.ZERO);
        var balance = new AtomicReference<>(previousBalance);
        var counter = new int[]{0};

        var lines = rows.stream()
            .map(row -> {
                counter[0]++;
                var isDebit = CashTransactionType.DEBIT.equals(row.type());
                return CashTransactionReportLine.builder()
                    .no(counter[0])
                    .date(row.date())
                    .refNo(row.refNo())
                    .type(row.type())
                    .name(row.journalClassName())
                    .memo(row.memo())
                    .debit(isDebit ? row.amount() : null)
                    .credit(!isDebit ? row.amount() : null)
                    .balance(isDebit ?
                        balance.updateAndGet(b -> b.add(row.amount())) :
                        balance.updateAndGet(b -> b.subtract(row.amount()))
                    )
                    .build();
            })
            .toList();

        return CashTransactionReportResponse.builder()
            .initCashOnHand(previousBalance)
            .lines(lines)
            .build();
    }
}
