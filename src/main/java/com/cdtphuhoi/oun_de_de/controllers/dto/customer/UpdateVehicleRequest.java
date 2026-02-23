package com.cdtphuhoi.oun_de_de.controllers.dto.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateVehicleRequest {
    @NotBlank
    private String vehicleType;

    private String licensePlate;
}
