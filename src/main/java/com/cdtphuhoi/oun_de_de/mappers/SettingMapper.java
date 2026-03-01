package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.common.UnitType;
import com.cdtphuhoi.oun_de_de.controllers.dto.settings.CreateUnitRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.settings.CreateWarehouseRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.settings.UpdateUnitRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.settings.UpdateWarehouseRequest;
import com.cdtphuhoi.oun_de_de.entities.Unit;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.entities.Warehouse;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateUnitData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateWarehouseData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UnitResult;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UpdateUnitData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UpdateWarehouseData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.WarehouseResult;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper(
    uses = MapperHelpers.class,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    builder = @Builder(disableBuilder = true)
)
public interface SettingMapper {
    SettingMapper INSTANCE = Mappers.getMapper(SettingMapper.class);

    CreateUnitData toCreateUnitData(CreateUnitRequest request);

    UpdateUnitData toUpdateUnitData(UpdateUnitRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Unit toUnit(CreateUnitData createUnitData, User employeeUser);

    void updateUnit(UpdateUnitData updateUnitData, @MappingTarget Unit unit);

    UnitResult toUnitResult(Unit unitDb);

    List<UnitResult> toListUnitResult(List<Unit> results);

    @ValueMapping(target = "COUNT", source = "count")
    @ValueMapping(target = "LENGTH", source = "length")
    @ValueMapping(target = "WEIGHT", source = "weight")
    @ValueMapping(target = "VOLUME", source = "volume")
    @ValueMapping(target = "TIME", source = "time")
    UnitType stringToUnitType(String source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Warehouse toWarehouse(CreateWarehouseData createWarehouseData, User employeeUser);

    WarehouseResult toWarehouseResult(Warehouse warehouseDb);

    List<WarehouseResult> toListWarehouseResult(List<Warehouse> results);

    CreateWarehouseData toWarehouseDate(CreateWarehouseRequest request);

    UpdateWarehouseData toUpdateWarehouseData(UpdateWarehouseRequest request);

    void updateWarehouse(UpdateWarehouseData updateWarehouseData, @MappingTarget Warehouse warehouse);
}
