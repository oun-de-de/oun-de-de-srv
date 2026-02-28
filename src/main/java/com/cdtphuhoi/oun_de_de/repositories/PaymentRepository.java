package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Payment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findAllByCycleId(String cycleId, Sort by);

    @Query(
        value = """
                SELECT FUNCTION('date', p.paymentDate),
                       COALESCE(SUM(p.amount), 0)
                FROM Payment p
                WHERE p.paymentDate >= :fromDateTime
                  AND p.paymentDate <  :toDateTime
                  AND p.orgId = :orgId
                GROUP BY FUNCTION('date', p.paymentDate)
                ORDER BY FUNCTION('date', p.paymentDate)
            """
    )
    List<Object[]> sumAmountByDateRange(
        @Param("fromDateTime") LocalDateTime fromDate,
        @Param("toDateTime") LocalDateTime toDate,
        @Param("orgId") String orgId
    );
}
