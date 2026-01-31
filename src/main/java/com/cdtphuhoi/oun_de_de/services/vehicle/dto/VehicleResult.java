package com.cdtphuhoi.oun_de_de.services.vehicle.dto;

import com.cdtphuhoi.oun_de_de.common.VehicleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleResult {
    private String id;

    private VehicleType vehicleType;

    private String licensePlate;
}
