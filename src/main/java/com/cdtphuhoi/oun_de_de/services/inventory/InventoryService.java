package com.cdtphuhoi.oun_de_de.services.inventory;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_PADDING_LENGTH;
import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.paddingZero;
import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.common.StockTransactionType;
import com.cdtphuhoi.oun_de_de.entities.InventoryItem;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction_;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.InventoryItemRepository;
import com.cdtphuhoi.oun_de_de.repositories.StockTransactionRepository;
import com.cdtphuhoi.oun_de_de.repositories.UnitRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.CreateItemData;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.InventoryItemResult;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.StockTransactionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService implements OrgManagementService {

    private final InventoryItemRepository inventoryItemRepository;

    private final StockTransactionRepository stockTransactionRepository;

    private final UnitRepository unitRepository;

    public List<InventoryItemResult> findAllItems() {
        var items = inventoryItemRepository.findAll();
        return MapperHelpers.getInventoryMapper().toListInventoryItemResult(items);
    }

    public InventoryItemResult createItem(CreateItemData createItemData, User usr) {
        var unit = Optional.ofNullable(createItemData.getUnitId())
            .map(unitId -> unitRepository.findOneById(unitId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Unit [id=%s] not found", createItemData.getUnitId())
                    )
                )
            )
            .orElse(null);
        var maxCurrentRefCode = Optional.ofNullable(inventoryItemRepository.findMaxRefCode(usr.getOrgId()))
            .orElse(0L);
        var qty = Optional.ofNullable(createItemData.getQuantityOnHand())
            .orElse(BigDecimal.ZERO);
        var item = InventoryItem.builder()
            .orgId(usr.getOrgId())
            .code(String.format("ITEM%s", paddingZero(BigInteger.valueOf(maxCurrentRefCode + 1), DEFAULT_PADDING_LENGTH)))
            .name(createItemData.getName())
            .type(MapperHelpers.getInventoryMapper().stringToItemType(createItemData.getType()))
            .unit(unit)
            .quantityOnHand(qty)
            .alertThreshold(createItemData.getAlertThreshold())
            .build();

        log.info("Creating item");
        var itemDb = inventoryItemRepository.save(item);
        log.info("Created item, id = {}", itemDb.getId());
        if (!BigDecimal.ZERO.equals(qty)) {
            initStockTransaction(itemDb, qty, usr);
        }
        return MapperHelpers.getInventoryMapper().toInventoryItemResult(itemDb);
    }

    private void initStockTransaction(InventoryItem item, BigDecimal quantity, User usr) {
        var stockTransaction = StockTransaction.builder()
            .orgId(item.getOrgId())
            .item(item)
            .quantity(quantity)
            .type(StockTransactionType.IN)
            .reason(StockTransactionReason.PURCHASE)
            .createdAt(cambodiaNow())
            .createdBy(usr)
            .build();

        log.info("Creating stock transaction");
        var stockTransactionDb = stockTransactionRepository.save(stockTransaction);
        log.info("Created stock transaction, id = {}", stockTransactionDb.getId());
    }

    public List<StockTransactionResult> findTransactionsByItem(String itemId) {
        var transactions = stockTransactionRepository.findByItemId(
            itemId,
            Sort.by(Sort.Direction.DESC, StockTransaction_.CREATED_AT)
        );
        return MapperHelpers.getInventoryMapper().toListStockTransactionResult(transactions);
    }
}
