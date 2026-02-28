package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.entities.PaymentTermCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface PaymentTermCycleRepository extends JpaRepository<PaymentTermCycle, String>,
    JpaSpecificationExecutor<PaymentTermCycle> {

    Optional<PaymentTermCycle> findOneById(String id);

    @Query(
        value = """
                SELECT COALESCE(SUM(total_amount), 0)
                FROM payment_term_cycle
                WHERE org_id = :orgId;
            """,
        nativeQuery = true
    )
    BigDecimal sumAmount(@Param("orgId") String orgId);

    Long countByStatus(PaymentTermCycleStatus status);
}
