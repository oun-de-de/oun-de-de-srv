package com.cdtphuhoi.oun_de_de.services.vehicle;

import com.cdtphuhoi.oun_de_de.repositories.VehicleRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.VehicleResult;
import com.cdtphuhoi.oun_de_de.utils.mappers.VehicleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService implements OrgManagementService {
    private final VehicleRepository vehicleRepository;

    @Transactional(readOnly = true)
    public List<VehicleResult> findAll() {
        return VehicleMapper.INSTANCE.toListVehicleResult(vehicleRepository.findAll());
    }
}
