package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String>,
    JpaSpecificationExecutor<Invoice> {

    @Query(
        value = """
                SELECT MAX(CAST(SUBSTRING(ref_no, 3) AS UNSIGNED)) AS max_no
                FROM invoice
                WHERE ref_no LIKE 'IN%' and org_id = :orgId;
            """,
        nativeQuery = true
    )
    Long findMaxRefNo(@Param("orgId") String orgId);
}
