package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.entities.StockTransaction;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord;
import com.cdtphuhoi.oun_de_de.services.reports.dto.DailyBoughtItem;
import com.cdtphuhoi.oun_de_de.services.reports.dto.DailySoldProduct;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    uses = MapperHelpers.class,
    builder = @Builder(disableBuilder = true)
)
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    DailySoldProduct toDailySoldProduct(WeightRecord weightRecord);

    List<DailySoldProduct> toListDailySoldProducts(List<WeightRecord> weightRecords);

    @Mapping(target = "itemName", source = "stockTransaction.item.name")
    DailyBoughtItem toDailyBoughtItem(StockTransaction stockTransaction);

    List<DailyBoughtItem> toListDailyBoughtItems(List<StockTransaction> stockTransactions);
}
