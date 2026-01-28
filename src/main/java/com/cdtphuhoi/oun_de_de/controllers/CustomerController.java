package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateCustomerRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CustomerRequestResponse;
import com.cdtphuhoi.oun_de_de.utils.mappers.CustomerMapper;
import com.cdtphuhoi.oun_de_de.services.customer.CustomerManagementService;
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
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerManagementService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerRequestResponse>> listCustomers() {
        var customers = customerService.findBy();
        return ResponseEntity.ok(
            CustomerMapper.INSTANCE.toListCustomerRequestResponse(customers)
        );
    }


    @PostMapping
    public ResponseEntity<CustomerRequestResponse> createCustomer(
        @Valid @RequestBody CreateCustomerRequest request) {
        var customer = customerService.create(CustomerMapper.INSTANCE.toCreateCustomerData(request));
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(CustomerMapper.INSTANCE.toCustomerRequestResponse(customer));
    }
}
