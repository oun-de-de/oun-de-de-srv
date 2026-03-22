package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.vehicle.UpdateVehicleRequestV2;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.vehicle.VehicleService;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.VehicleResult;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v2/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PutMapping("/{vehicleId}")
    public ResponseEntity<VehicleResult> updateVehicle(
        @PathVariable String vehicleId,
        @Valid @RequestBody UpdateVehicleRequestV2 request) {
        var result = vehicleService.updateVehicleV2(
            vehicleId,
            MapperHelpers.getVehicleMapper().toUpdateVehicleDataV2(request)
        );
        return ResponseEntity
            .ok(result);
    }
}
