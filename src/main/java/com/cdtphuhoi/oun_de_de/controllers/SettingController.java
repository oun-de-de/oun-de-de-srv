package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.settings.CreateUnitRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.settings.CreateWarehouseRequest;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.settings.SettingService;
import com.cdtphuhoi.oun_de_de.services.settings.dto.UnitResult;
import com.cdtphuhoi.oun_de_de.services.settings.dto.WarehouseResult;
import com.cdtphuhoi.oun_de_de.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/settings")
public class SettingController {

    private final SettingService settingService;

    private final ControllerUtils controllerUtils;

    @GetMapping("/units")
    public ResponseEntity<List<UnitResult>> listProducts() {
        return ResponseEntity.ok(
            settingService.findAllUnits()
        );
    }

    @PostMapping("/units")
    public ResponseEntity<UnitResult> createUnit(
        @Valid @RequestBody CreateUnitRequest request) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = settingService.createUnit(
            MapperHelpers.getSettingMapper().toCreateUnitData(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    @GetMapping("/warehouses")
    public ResponseEntity<List<WarehouseResult>> listWarehouses() {
        return ResponseEntity.ok(
            settingService.findAllWarehouses()
        );
    }

    @PostMapping("/warehouses")
    public ResponseEntity<WarehouseResult> createWarehouse(
        @Valid @RequestBody CreateWarehouseRequest request) {
        var usr = controllerUtils.getCurrentSignedInUser();
        var result = settingService.createWarehouse(
            MapperHelpers.getSettingMapper().toWarehouseDate(request),
            usr
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }
}
