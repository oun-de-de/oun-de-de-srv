package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.common.LoanStatus;
import com.cdtphuhoi.oun_de_de.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String>,
    JpaSpecificationExecutor<Loan> {

    Optional<Loan> findOneById(String id);

    Long countByStatus(LoanStatus status);

    @Query(
        value = """
                SELECT MAX(CAST(SUBSTRING(code, 5) AS UNSIGNED)) AS max_no
                FROM loan
                WHERE code LIKE 'LOAN%' and org_id = :orgId;
            """,
        nativeQuery = true
    )
    Long findMaxRefCode(@Param("orgId") String orgId);
}
