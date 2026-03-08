package com.cdtphuhoi.oun_de_de.services.reports;

import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.entities.Invoice_;
import com.cdtphuhoi.oun_de_de.entities.Payment;
import com.cdtphuhoi.oun_de_de.entities.Payment_;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction_;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord_;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.StockTransactionRepository;
import com.cdtphuhoi.oun_de_de.repositories.WeightRecordRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.reports.dto.DailyReportResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
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

    private final WeightRecordRepository weightRecordRepository;

    private final StockTransactionRepository stockTransactionRepository;

    private final EntityManager entityManager;

    public DailyReportResponse getReport(LocalDate date, String orgId) {
        var soldProducts = weightRecordRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> root.get(WeightRecord_.PRODUCT_NAME).isNotNull(),
                (root, query, cb) -> root.get(WeightRecord_.PRICE_PER_PRODUCT).isNotNull(),
                (root, query, cb) -> root.get(WeightRecord_.QUANTITY).isNotNull(),
                (root, query, cb) -> buildDatePredicate(date, cb, root.get(WeightRecord_.INVOICE).get(Invoice_.DATE))

            )
        );
        var totalRevenue = soldProducts.stream()
            .map(WeightRecord::getAmount)
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
                    root.fetch(StockTransaction_.item, JoinType.INNER);
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
            .soldProducts(MapperHelpers.getReportMapper().toListDailySoldProducts(soldProducts))
            .boughtItems(MapperHelpers.getReportMapper().toListDailyBoughtItems(boughtItems))
            .totalRevenue(totalRevenue)
            .totalCashReceive(totalCashReceive)
            .totalExpense(totalExpense)
            .build();
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
}
