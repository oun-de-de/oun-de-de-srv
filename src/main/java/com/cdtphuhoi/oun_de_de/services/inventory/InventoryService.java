package com.cdtphuhoi.oun_de_de.services.inventory;

import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.InventoryItemRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.InventoryItemResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService implements OrgManagementService {

    private final InventoryItemRepository inventoryItemRepository;

    public List<InventoryItemResult> findAllItems() {
        var items = inventoryItemRepository.findAll();
        return MapperHelpers.getInventoryMapper().toListInventoryItemResult(items);
    }
}
