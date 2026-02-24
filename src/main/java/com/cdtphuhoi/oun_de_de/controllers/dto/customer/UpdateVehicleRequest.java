package com.cdtphuhoi.oun_de_de.controllers.dto.customer;

import com.cdtphuhoi.oun_de_de.common.VehicleType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateVehicleRequest {
    @NotBlank
    @ValueOfEnum(enumClass = VehicleType.class)
    private String vehicleType;

    private String licensePlate;
}
