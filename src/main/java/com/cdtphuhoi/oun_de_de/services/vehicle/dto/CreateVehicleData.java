package com.cdtphuhoi.oun_de_de.services.vehicle.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVehicleData {
    private String vehicleType;

    private String licensePlate;
}
