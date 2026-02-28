package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.EquipmentBorrow;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface EquipmentBorrowRepository extends JpaRepository<EquipmentBorrow, String>,
    JpaSpecificationExecutor<EquipmentBorrow> {
    List<EquipmentBorrow> findByItemId(String itemId, Sort sort);

    Optional<EquipmentBorrow> findOneById(String id);

}
