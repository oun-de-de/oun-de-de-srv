package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.StockTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, String>,
    JpaSpecificationExecutor<StockTransaction> {
    List<StockTransaction> findByItemId(String itemId, Sort sort);

    @Query(
        value = """
                SELECT COALESCE(SUM(expense), 0)
                FROM StockTransaction
                WHERE reason = com.cdtphuhoi.oun_de_de.common.StockTransactionReason.PURCHASE
                  AND orgId = :orgId
            """
    )
    BigDecimal sumExpenses(String orgId);

    @Query(
        value = """
                SELECT FUNCTION('date', createdAt),
                       COALESCE(SUM(expense), 0)
                FROM StockTransaction
                WHERE createdAt >= :fromDateTime
                  AND createdAt <  :toDateTime
                  AND orgId = :orgId
                GROUP BY FUNCTION('date', createdAt)
                ORDER BY FUNCTION('date', createdAt)
            """
    )
    List<Object[]> sumExpenseByDateRange(
        @Param("fromDateTime") LocalDateTime fromDate,
        @Param("toDateTime") LocalDateTime toDate,
        @Param("orgId") String orgId
    );
}
