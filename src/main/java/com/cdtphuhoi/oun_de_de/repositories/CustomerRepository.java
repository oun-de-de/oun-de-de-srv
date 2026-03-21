package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;

@Repository
public interface CustomerRepository extends
    BaseRepository<Customer>,
    JpaRepository<Customer, String>,
    JpaSpecificationExecutor<Customer> {

    @Query(
        value = """
                SELECT MAX(CAST(SUBSTRING(code, 4) AS UNSIGNED)) AS max_no
                FROM customer
                WHERE code LIKE 'CUS%' and org_id = :orgId;
            """,
        nativeQuery = true
    )
    Long findMaxRefCode(@Param("orgId") String orgId);

    boolean existsByCode(String code);

    List<Customer> findAllByIdIn(Collection<String> customerIds);
}
