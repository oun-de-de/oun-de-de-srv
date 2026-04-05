package com.cdtphuhoi.oun_de_de.services.settings;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CurrencyRepository;
import com.cdtphuhoi.oun_de_de.repositories.SupplierRepository;
import com.cdtphuhoi.oun_de_de.repositories.UnitRepository;
import com.cdtphuhoi.oun_de_de.repositories.WarehouseRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateCurrencyData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateSupplierData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateUnitData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateWarehouseData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CurrencyResult;
import com.cdtphuhoi.oun_de_de.services.settings.dto.SupplierResult;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UnitResult;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UpdateUnitData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UpdateSupplierData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UpdateWarehouseData;
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

    private final CurrencyRepository currencyRepository;

    private final SupplierRepository supplierRepository;

    public UnitResult createUnit(CreateUnitData createUnitData, User usr) {
        var unit = MapperHelpers.getSettingMapper().toUnit(createUnitData, usr);
        log.info("Creating unit");
        var unitDb = unitRepository.save(unit);
        log.info("Created unit, id = {}", unitDb.getId());
        return MapperHelpers.getSettingMapper().toUnitResult(unitDb);
    }

    public UnitResult updateUnit(String unitId, UpdateUnitData updateUnitData) {
        var unit = unitRepository.findOneById(unitId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Unit [id=%s] not found", unitId)
                )
            );
        MapperHelpers.getSettingMapper().updateUnit(updateUnitData, unit);
        var unitDb = unitRepository.save(unit);
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

    public WarehouseResult updateWarehouse(String warehouseId, UpdateWarehouseData updateWarehouseData) {
        var warehouse = warehouseRepository.findOneById(warehouseId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Warehouse [id=%s] not found", warehouseId)
                )
            );
        MapperHelpers.getSettingMapper().updateWarehouse(updateWarehouseData, warehouse);
        var warehouseDb = warehouseRepository.save(warehouse);
        return MapperHelpers.getSettingMapper().toWarehouseResult(warehouseDb);
    }

    public List<WarehouseResult> findAllWarehouses() {
        var results = warehouseRepository.findAll();
        return MapperHelpers.getSettingMapper().toListWarehouseResult(results);
    }

    public List<CurrencyResult> findAllCurrencies() {
        var results = currencyRepository.findAll();
        return MapperHelpers.getSettingMapper().toListCurrencyResult(results);
    }

    public CurrencyResult createCurrency(CreateCurrencyData createCurrencyData, User usr) {
        var currency = MapperHelpers.getSettingMapper().toCurrency(createCurrencyData);
        currency.setOrgId(usr.getOrgId());
        log.info("Creating currency");
        var currencyDb = currencyRepository.save(currency);
        log.info("Created currency, id = {}", currencyDb.getId());
        return MapperHelpers.getSettingMapper().toCurrencyResult(currencyDb);
    }

    public List<SupplierResult> findAllSuppliers() {
        var results = supplierRepository.findAll();
        return MapperHelpers.getSettingMapper().toListSupplierResult(results);
    }

    public SupplierResult createSupplier(CreateSupplierData createSupplierData, User usr) {
        var supplier = MapperHelpers.getSettingMapper().toSupplier(createSupplierData);
        supplier.setOrgId(usr.getOrgId());
        log.info("Creating supplier");
        var supplierDb = supplierRepository.save(supplier);
        log.info("Created supplier, id = {}", supplierDb.getId());
        return MapperHelpers.getSettingMapper().toSupplierResult(supplierDb);
    }

    public SupplierResult updateSupplier(String supplierId, UpdateSupplierData updateSupplierData) {
        var supplier = supplierRepository.findOneById(supplierId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Supplier [id=%s] not found", supplierId)
                )
            );
        MapperHelpers.getSettingMapper().updateSupplier(updateSupplierData, supplier);
        var supplierDb = supplierRepository.save(supplier);
        return MapperHelpers.getSettingMapper().toSupplierResult(supplierDb);
    }
}
