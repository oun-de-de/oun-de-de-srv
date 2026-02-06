package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateCustomerRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateVehicleRequest;
import com.cdtphuhoi.oun_de_de.services.customer.CustomerService;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerResult;
import com.cdtphuhoi.oun_de_de.services.vehicle.VehicleService;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.VehicleResult;
import com.cdtphuhoi.oun_de_de.utils.mappers.MapperHelpers;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<Page<CustomerResult>> listCustomers(
        @RequestParam(required = false) String name,
        Pageable pageable
    ) {
        var result = customerService.findBy(name, pageable);
        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<CustomerResult> createCustomer(
        @Valid @RequestBody CreateCustomerRequest request) {
        var result = customerService.create(
            MapperHelpers.getCustomerMapper().toCreateCustomerData(request)
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    @GetMapping("/{customerId}/vehicles")
    public ResponseEntity<List<VehicleResult>> listVehiclesByCustomerId(
        @PathVariable String customerId
    ) {
        return ResponseEntity.ok(vehicleService.findByCustomer(customerId));
    }

    @PostMapping("/{customerId}/vehicles")
    public ResponseEntity<VehicleResult> createVehicle(
        @PathVariable String customerId,
        @Valid @RequestBody CreateVehicleRequest request) {
        var result = vehicleService.createVehicle(
            customerId,
            MapperHelpers.getVehicleMapper().toCreateVehicleData(request)
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }
}
