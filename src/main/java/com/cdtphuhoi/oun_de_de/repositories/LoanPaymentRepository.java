package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.LoanPayment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanPaymentRepository extends JpaRepository<LoanPayment, String>,
    JpaSpecificationExecutor<LoanPayment> {
    List<LoanPayment> findAllByLoanId(String loanId, Sort by);

    @Query(
        value = """
                SELECT MAX(CAST(SUBSTRING(code, 5) AS UNSIGNED)) AS max_no
                FROM loan_payment
                WHERE code LIKE 'LPAY%' and org_id = :orgId;
            """,
        nativeQuery = true
    )
    Long findMaxRefCode(@Param("orgId") String orgId);
}
