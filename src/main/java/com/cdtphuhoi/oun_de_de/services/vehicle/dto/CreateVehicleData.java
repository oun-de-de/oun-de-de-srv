package com.cdtphuhoi.oun_de_de.services.vehicle.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateVehicleData {
    private String vehicleType;

    private String licensePlate;
}
