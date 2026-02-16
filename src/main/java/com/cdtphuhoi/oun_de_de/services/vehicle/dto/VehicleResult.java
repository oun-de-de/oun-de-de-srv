package com.cdtphuhoi.oun_de_de.services.vehicle.dto;

import com.cdtphuhoi.oun_de_de.common.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleResult {
    private String id;

    private VehicleType vehicleType;

    private String licensePlate;
}
