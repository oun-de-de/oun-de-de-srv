package com.cdtphuhoi.oun_de_de.services.settings;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.UnitRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.settings.dto.CreateUnitData;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UnitResult;
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

    public UnitResult createUnit(CreateUnitData createUnitData, User usr) {
        var unit = MapperHelpers.getSettingMapper().toUnit(createUnitData, usr);
        log.info("Creating unit");
        var unitDb = unitRepository.save(unit);
        log.info("Created unit, id = {}", unitDb.getId());
        return MapperHelpers.getSettingMapper().toUnitResult(unitDb);
    }

    public List<UnitResult> findAll() {
        var results = unitRepository.findAll();
        return MapperHelpers.getSettingMapper().toListUnitResult(results);
    }
}
