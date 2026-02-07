package com.cdtphuhoi.oun_de_de.services.customer;

import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.ProductRepository;
import com.cdtphuhoi.oun_de_de.repositories.ProductSettingRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.repositories.WarehouseRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateProductSettingData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerDetailsResult;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerResult;
import com.cdtphuhoi.oun_de_de.services.customer.dto.ProductSettingResult;
import com.cdtphuhoi.oun_de_de.services.customer.dto.UpdateCustomerData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService implements OrgManagementService {

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ProductSettingRepository productSettingRepository;

    private final WarehouseRepository warehouseRepository;

    public CustomerResult create(CreateCustomerData createCustomerData) {
        var employee = userRepository.findOneById(createCustomerData.getEmployeeId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Employee [id=%s] not found", createCustomerData.getEmployeeId())
                )
            );
        var referer = Optional.ofNullable(createCustomerData.getReferredById())
            .map(referredById -> customerRepository.findOneById(referredById)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Customer [id=%s] not found", referredById)
                    )
                )
            )
            .orElse(null);
        var warehouse = Optional.ofNullable(createCustomerData.getWarehouseId())
            .map(warehouseId -> warehouseRepository.findOneById(warehouseId)
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Warehouse [id=%s] not found", warehouseId)
                    )
                )
            )
            .orElse(null);
        var customer = MapperHelpers.getCustomerMapper().toCustomer(createCustomerData, employee);
        customer.setReferredBy(referer);
        customer.setWarehouse(warehouse);
        log.info("Creating customer {}", customer.getName());
        var customerDb = customerRepository.save(customer);
        log.info("Created customer, id = {}", customerDb.getId());
        return MapperHelpers.getCustomerMapper().toCustomerResult(customerDb);
    }

    public Page<CustomerResult> findBy(String name, Pageable pageable) {
        if (StringUtils.isBlank(name)) {
            name = "";
        }
        var page = customerRepository.findByNameContainingIgnoreCase(name, pageable);
        return page.map(MapperHelpers.getCustomerMapper()::toCustomerResult);
    }

    public CustomerDetailsResult getCustomerDetails(String customerId) {
        var customer = customerRepository.findOneById(customerId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Customer [id=%s] not found", customerId)
                )
            );
        return MapperHelpers.getCustomerMapper().toCustomerDetailsResult(customer);
    }

    public CustomerResult update(String customerId, UpdateCustomerData updateCustomerData) {
        var customer = customerRepository.findOneById(customerId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Customer [id=%s] not found", customerId)
                )
            );
        if (updateCustomerData.getEmployeeId() != null &&
            !customer.getEmployee().getId().equals(updateCustomerData.getEmployeeId())) {
            var employee = userRepository.findOneById(updateCustomerData.getEmployeeId())
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Employee [id=%s] not found", updateCustomerData.getEmployeeId())
                    )
                );
            customer.setEmployee(employee);
        }
        if (updateCustomerData.getReferredById() != null &&
            (customer.getReferredBy() == null ||
                !customer.getReferredBy().getId().equals(updateCustomerData.getReferredById()))) {
            var referer = customerRepository.findOneById(updateCustomerData.getReferredById())
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Customer [id=%s] not found", updateCustomerData.getReferredById())
                    )
                );
            customer.setReferredBy(referer);
        }
        if (updateCustomerData.getWarehouseId() != null &&
            (customer.getWarehouse() == null ||
                !customer.getWarehouse().getId().equals(updateCustomerData.getWarehouseId()))) {
            var warehouse = warehouseRepository.findOneById(updateCustomerData.getWarehouseId())
                .orElseThrow(
                    () -> new ResourceNotFoundException(
                        String.format("Warehouse [id=%s] not found", updateCustomerData.getWarehouseId())
                    )
                );
            customer.setWarehouse(warehouse);
        }
        MapperHelpers.getCustomerMapper().updateCustomer(customer, updateCustomerData);
        var updatedCustomer = customerRepository.save(customer);
        return MapperHelpers.getCustomerMapper().toCustomerResult(updatedCustomer);
    }

    public ProductSettingResult createProductSetting(
        String customerId,
        CreateProductSettingData createProductSettingData
    ) {
        var customer = customerRepository.findOneById(customerId)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Customer [id=%s] not found", customerId)
                )
            );
        var product = productRepository.findOneById(createProductSettingData.getProductId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Product [id=%s] not found", createProductSettingData.getProductId())
                )
            );
        var productSetting = MapperHelpers.getCustomerMapper().toProductSetting(
            createProductSettingData,
            customer,
            product
        );
        var productSettingDb = productSettingRepository.save(productSetting);
        return ProductSettingResult.builder()
            .productId(product.getId())
            .customerId(customer.getId())
            .price(productSettingDb.getPrice())
            .weight(productSettingDb.getWeight())
            .build();
    }

    public List<ProductSettingResult> getProductSettings(String customerId) {
        return productSettingRepository.findAllByCustomerId(customerId).stream()
            .map(proSet -> ProductSettingResult.builder()
                .productId(proSet.getProductSettingId().getProductId())
                .customerId(proSet.getProductSettingId().getCustomerId())
                .price(proSet.getPrice())
                .weight(proSet.getWeight())
                .build())
            .toList();
    }
}
