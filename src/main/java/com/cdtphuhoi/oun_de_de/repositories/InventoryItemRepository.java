package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {

    @Query(
        value = """
                SELECT MAX(CAST(SUBSTRING(code, 5) AS UNSIGNED)) AS max_no
                FROM inventory_item
                WHERE code LIKE 'ITEM%' and org_id = :orgId;
            """,
        nativeQuery = true
    )
    Long findMaxRefCode(@Param("orgId") String orgId);
}
