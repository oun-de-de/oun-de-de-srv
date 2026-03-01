package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findOneById(String id);

    @Query(
        value = """
                SELECT MAX(CAST(SUBSTRING(ref_no, 5) AS UNSIGNED)) AS max_no
                FROM product
                WHERE ref_no LIKE 'PROD%' and org_id = :orgId;
            """,
        nativeQuery = true
    )
    Long findMaxRefNo(@Param("orgId") String orgId);
}
