package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.BorrowStatus;
import com.cdtphuhoi.oun_de_de.common.ItemType;
import com.cdtphuhoi.oun_de_de.common.StockTransactionReason;
import com.cdtphuhoi.oun_de_de.common.StockTransactionType;
import com.cdtphuhoi.oun_de_de.controllers.dto.inventory.CreateEquipmentBorrowRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.inventory.CreateItemRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.inventory.UpdateStockTransactionRequest;
import com.cdtphuhoi.oun_de_de.entities.EquipmentBorrow;
import com.cdtphuhoi.oun_de_de.entities.InventoryItem;
import com.cdtphuhoi.oun_de_de.entities.StockTransaction;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.CreateEquipmentBorrowData;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.CreateItemData;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.CreateStockTransactionData;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.EquipmentBorrowResult;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.InventoryItemResult;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.StockTransactionResult;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    uses = {
        SettingMapper.class
    },
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface InventoryMapper {
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    InventoryItemResult toInventoryItemResult(InventoryItem item);

    List<InventoryItemResult> toListInventoryItemResult(List<InventoryItem> items);

    CreateItemData toCreateItemData(CreateItemRequest request);

    @ValueMapping(target = "CONSUMABLE", source = "consumable")
    @ValueMapping(target = "EQUIPMENT", source = "equipment")
    ItemType stringToItemType(String source);

    @ValueMapping(target = "PURCHASE", source = "purchase")
    @ValueMapping(target = "CONSUME", source = "consume")
    @ValueMapping(target = "BORROW", source = "borrow")
    @ValueMapping(target = "RETURN", source = "return")
    StockTransactionReason stringToStockTransactionReason(String source);

    @ValueMapping(target = "IN", source = "in")
    @ValueMapping(target = "OUT", source = "out")
    StockTransactionType stringToStockTransactionType(String source);

    @ValueMapping(target = "BORROWED", source = "borrowed")
    @ValueMapping(target = "RETURNED", source = "returned")
    BorrowStatus stringToBorrowStatus(String source);

    @Mapping(target = "itemId", source = "transaction.item.id")
    @Mapping(target = "equipmentBorrowId", source = "transaction.equipmentBorrow.id")
    @Mapping(target = "createdById", source = "transaction.createdBy.id")
    StockTransactionResult toStockTransactionResult(StockTransaction transaction);

    List<StockTransactionResult> toListStockTransactionResult(List<StockTransaction> transactions);

    CreateStockTransactionData toCreateStockTransactionData(UpdateStockTransactionRequest request);

    CreateEquipmentBorrowData toCreateEquipmentBorrowData(CreateEquipmentBorrowRequest equipmentBorrowRequest);

    EquipmentBorrowResult toEquipmentBorrowResult(EquipmentBorrow equipmentBorrow);

    List<EquipmentBorrowResult> toListEquipmentBorrowResult(List<EquipmentBorrow> equipmentBorrows);
}
