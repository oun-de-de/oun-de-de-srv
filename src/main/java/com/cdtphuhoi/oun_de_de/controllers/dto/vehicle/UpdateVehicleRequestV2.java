package com.cdtphuhoi.oun_de_de.controllers.dto.vehicle;

import com.cdtphuhoi.oun_de_de.common.VehicleType;
import com.cdtphuhoi.oun_de_de.validators.ValueOfEnum;
import lombok.Data;
import java.util.UUID;

@Data
public class UpdateVehicleRequestV2 {

    /*
     * CUSTOMER
     */
    private UUID customerId;

    @ValueOfEnum(enumClass = VehicleType.class)
    private String vehicleType;

    private String licensePlate;
}
