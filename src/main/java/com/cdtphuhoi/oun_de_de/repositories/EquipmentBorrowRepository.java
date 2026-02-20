package com.cdtphuhoi.oun_de_de.repositories;

import com.cdtphuhoi.oun_de_de.entities.EquipmentBorrow;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EquipmentBorrowRepository extends JpaRepository<EquipmentBorrow, String> {
    List<EquipmentBorrow> findByItemId(String itemId, Sort sort);
}
