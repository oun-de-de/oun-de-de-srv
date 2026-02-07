package com.cdtphuhoi.oun_de_de.services.settings;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.UnitRepository;
import com.cdtphuhoi.oun_de_de.repositories.WarehouseRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateUnitData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateWarehouseData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UnitResult;
import com.cdtphuhoi.oun_de_de.services.settings.dto.WarehouseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SettingService implements OrgManagementService {

    private final UnitRepository unitRepository;

    private final WarehouseRepository warehouseRepository;

    public UnitResult createUnit(CreateUnitData createUnitData, User usr) {
        var unit = MapperHelpers.getSettingMapper().toUnit(createUnitData, usr);
        log.info("Creating unit");
        var unitDb = unitRepository.save(unit);
        log.info("Created unit, id = {}", unitDb.getId());
        return MapperHelpers.getSettingMapper().toUnitResult(unitDb);
    }

    public List<UnitResult> findAllUnits() {
        var results = unitRepository.findAll();
        return MapperHelpers.getSettingMapper().toListUnitResult(results);
    }

    public WarehouseResult createWarehouse(CreateWarehouseData createWarehouseData, User usr) {
        var warehouse = MapperHelpers.getSettingMapper().toWarehouse(createWarehouseData, usr);
        log.info("Creating warehouse");
        var warehouseDb = warehouseRepository.save(warehouse);
        log.info("Created warehouse, id = {}", warehouseDb.getId());
        return MapperHelpers.getSettingMapper().toWarehouseResult(warehouseDb);
    }

    public List<WarehouseResult> findAllWarehouses() {
        var results = warehouseRepository.findAll();
        return MapperHelpers.getSettingMapper().toListWarehouseResult(results);
    }
}
