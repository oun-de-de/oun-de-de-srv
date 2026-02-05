package com.cdtphuhoi.oun_de_de.services.vehicle;

import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.VehicleRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.CreateVehicleData;
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
@Transactional
public class VehicleService implements OrgManagementService {
    private final VehicleRepository vehicleRepository;

    private final CustomerRepository customerRepository;

    public List<VehicleResult> findAll() {
        return VehicleMapper.INSTANCE.toListVehicleResult(vehicleRepository.findAll());
    }

    public List<VehicleResult> findByCustomer(String customerId) {
        return VehicleMapper.INSTANCE.toListVehicleResult(
            vehicleRepository.findAllByCustomerId(customerId)
        );
    }

    public VehicleResult createVehicle(String customerId, CreateVehicleData createVehicleData) {
        var customer = customerRepository.findOneById(customerId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Customer [id=%s] not found", customerId)
                )
            );

        var vehicle = VehicleMapper.INSTANCE.toVehicle(createVehicleData, customer);
        log.info("Creating vehicle for customer {}", customer.getName());
        var vehicleDb = vehicleRepository.save(vehicle);
        log.info("Created vehicle, id = {}", vehicleDb.getId());
        return VehicleMapper.INSTANCE.toVehicleResult(vehicleDb);
    }
}
