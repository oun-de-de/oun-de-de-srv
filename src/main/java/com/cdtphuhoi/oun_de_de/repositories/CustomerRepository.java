package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>,
    JpaSpecificationExecutor<Customer>, BaseRepository<Customer> {

    @Query(
        value = """
                SELECT MAX(CAST(SUBSTRING(code, 4) AS UNSIGNED)) AS max_no
                FROM customer
                WHERE code LIKE 'CUS%' and org_id = :orgId;
            """,
        nativeQuery = true
    )
    Long findMaxRefCode(@Param("orgId") String orgId);
}
