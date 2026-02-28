package com.cdtphuhoi.oun_de_de.services.inventory;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_PADDING_LENGTH;
import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.paddingZero;
import com.cdtphuhoi.oun_de_de.common.BorrowStatus;
import com.cdtphuhoi.oun_de_de.common.BorrowerType;
import com.cdtphuhoi.oun_de_de.common.ItemType;
import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.common.StockTransactionType;
import com.cdtphuhoi.oun_de_de.entities.EquipmentBorrow;
import com.cdtphuhoi.oun_de_de.entities.EquipmentBorrow_;
import com.cdtphuhoi.oun_de_de.entities.InventoryItem;
import com.cdtphuhoi.oun_de_de.entities.InventoryItem_;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction_;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.EquipmentBorrowRepository;
import com.cdtphuhoi.oun_de_de.repositories.InventoryItemRepository;
import com.cdtphuhoi.oun_de_de.repositories.StockTransactionRepository;
import com.cdtphuhoi.oun_de_de.repositories.UnitRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.CreateEquipmentBorrowData;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.CreateItemData;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.CreateStockTransactionData;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.EquipmentBorrowResult;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.InventoryItemResult;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.StockTransactionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.JoinType;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService implements OrgManagementService {

    private final InventoryItemRepository inventoryItemRepository;

    private final StockTransactionRepository stockTransactionRepository;

    private final EquipmentBorrowRepository equipmentBorrowRepository;

    private final CustomerRepository customerRepository;

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

        persistStockTransaction(stockTransaction);
    }

    public List<StockTransactionResult> findTransactionsByItem(String itemId) {
        var transactions = stockTransactionRepository.findByItemId(
            itemId,
            Sort.by(Sort.Direction.DESC, StockTransaction_.CREATED_AT)
        );
        return MapperHelpers.getInventoryMapper().toListStockTransactionResult(transactions);
    }

    public InventoryItemResult findItemById(String itemId) {
        var item = inventoryItemRepository.findOneById(itemId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Item [id=%s] not found", itemId)
                )
            );
        return MapperHelpers.getInventoryMapper().toInventoryItemResult(item);
    }

    public StockTransactionResult updateStockTransaction(String itemId, CreateStockTransactionData createStockTransactionData, User usr) {
        var transactionType = switch (createStockTransactionData.getReason()) {
            case PURCHASE -> StockTransactionType.IN;
            case CONSUME -> StockTransactionType.OUT;
            default -> throw new IllegalArgumentException("Invalid argument");
        };
        log.info("Internal stock in out {}, reason {}", itemId, createStockTransactionData.getReason());
        var item = inventoryItemRepository.findOneById(itemId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Item [id=%s] not found", itemId)
                )
            );
        updateItemQuantity(transactionType, createStockTransactionData.getQuantity(), item);

        var stockTransaction = StockTransaction.builder()
            .orgId(item.getOrgId())
            .item(item)
            .type(transactionType)
            .quantity(createStockTransactionData.getQuantity())
            .reason(createStockTransactionData.getReason())
            .memo(createStockTransactionData.getMemo())
            .createdAt(cambodiaNow())
            .createdBy(usr)
            .build();

        var stockTransactionDb = persistStockTransaction(stockTransaction);
        return MapperHelpers.getInventoryMapper().toStockTransactionResult(stockTransactionDb);
    }

    private void updateItemQuantity(StockTransactionType transactionType, BigDecimal requestQuantity, InventoryItem item) {
        var quantityForUpdate = StockTransactionType.IN.equals(transactionType) ?
            requestQuantity : requestQuantity.multiply(BigDecimal.valueOf(-1));
        var newQuantity = item.getQuantityOnHand().add(quantityForUpdate);
        if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(
                String.format("Item [id=%s] is out of stock", item.getId())
            );
        }
        item.setQuantityOnHand(newQuantity);
    }

    private StockTransaction persistStockTransaction(StockTransaction stockTransaction) {
        log.info("Creating stock transaction");
        var stockTransactionDb = stockTransactionRepository.save(stockTransaction);
        log.info("Created stock transaction, id = {}", stockTransactionDb.getId());
        return stockTransactionDb;
    }

    public StockTransactionResult createEquipmentBorrowing(
        String itemId,
        CreateEquipmentBorrowData createEquipmentBorrowData,
        User usr
    ) {
        var customer = customerRepository.findOneById(createEquipmentBorrowData.getCustomerId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Customer [id=%s] not found", createEquipmentBorrowData.getCustomerId())
                )
            );
        var item = inventoryItemRepository.findOneById(itemId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Item [id=%s] not found", itemId)
                )
            );
        if (!ItemType.EQUIPMENT.equals(item.getType())) {
            throw new BadRequestException(
                String.format("Item [id=%s] is not equipment", itemId)
            );
        }
        updateItemQuantity(StockTransactionType.OUT, createEquipmentBorrowData.getQuantity(), item);

        var equipmentBorrow = EquipmentBorrow.builder()
            .orgId(item.getOrgId())
            .item(item)
            .customer(customer)
            .quantity(createEquipmentBorrowData.getQuantity())
            .borrowDate(cambodiaNow())
            .expectedReturnDate(createEquipmentBorrowData.getExpectedReturnDate())
            .status(BorrowStatus.BORROWED)
            .build();

        var stockTransaction = StockTransaction.builder()
            .orgId(item.getOrgId())
            .item(item)
            .equipmentBorrow(equipmentBorrow)
            .quantity(equipmentBorrow.getQuantity())
            .type(StockTransactionType.OUT)
            .reason(StockTransactionReason.BORROW)
            .memo(createEquipmentBorrowData.getMemo())
            .createdAt(cambodiaNow())
            .createdBy(usr)
            .build();

        var stockTransactionDb = persistStockTransaction(stockTransaction);
        return MapperHelpers.getInventoryMapper().toStockTransactionResult(stockTransactionDb);
    }

    public List<EquipmentBorrowResult> findEquipmentBorrowingsByItem(String itemId) {
        var equipmentBorrows = equipmentBorrowRepository.findAll(
            Specification.allOf(
                (root, query, cb) -> cb.equal(root.get(EquipmentBorrow_.ITEM).get(InventoryItem_.ID), itemId),
                (root, query, cb) -> {
                    root.fetch(EquipmentBorrow_.CUSTOMER, JoinType.LEFT);
                    return null;
                }
            ),
            Sort.by(Sort.Direction.DESC, EquipmentBorrow_.BORROW_DATE)
        );
        return MapperHelpers.getInventoryMapper().toListEquipmentBorrowResult(equipmentBorrows);
    }

    public StockTransactionResult returnBorrowing(String itemId, String borrowingId, User usr) {
        var item = inventoryItemRepository.findOneById(itemId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Item [id=%s] not found", itemId)
                )
            );
        if (!ItemType.EQUIPMENT.equals(item.getType())) {
            throw new BadRequestException(
                String.format("Item [id=%s] is not equipment", itemId)
            );
        }
        var equipmentBorrow = equipmentBorrowRepository.findOneById(borrowingId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("EquipmentBorrow [id=%s] not found", borrowingId)
                )
            );
        if (!equipmentBorrow.getItem().getId().equals(itemId)) {
            throw new BadRequestException("Item with borrowing not match");
        }
        equipmentBorrow.setActualReturnDate(cambodiaNow());
        equipmentBorrow.setStatus(BorrowStatus.RETURNED);

        updateItemQuantity(StockTransactionType.IN, equipmentBorrow.getQuantity(), item);

        var stockTransaction = StockTransaction.builder()
            .orgId(item.getOrgId())
            .item(item)
            .equipmentBorrow(equipmentBorrow)
            .quantity(equipmentBorrow.getQuantity())
            .type(StockTransactionType.IN)
            .reason(StockTransactionReason.RETURN)
            .createdAt(cambodiaNow())
            .createdBy(usr)
            .build();

        var stockTransactionDb = persistStockTransaction(stockTransaction);
        return MapperHelpers.getInventoryMapper().toStockTransactionResult(stockTransactionDb);
    }
}
