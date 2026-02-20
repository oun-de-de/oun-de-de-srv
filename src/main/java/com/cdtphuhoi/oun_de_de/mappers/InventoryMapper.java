package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.entities.InventoryItem;
import com.cdtphuhoi.oun_de_de.services.inventory.dto.InventoryItemResult;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface InventoryMapper {
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    InventoryItemResult toInventoryItemResult(InventoryItem item);

    List<InventoryItemResult> toListInventoryItemResult(List<InventoryItem> items);
}
