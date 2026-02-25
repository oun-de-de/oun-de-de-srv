package com.cdtphuhoi.oun_de_de.services.customer;

import static com.cdtphuhoi.oun_de_de.utils.Utils.cambodiaNow;
import static com.cdtphuhoi.oun_de_de.utils.Utils.endOfDayInCambodia;
import static com.cdtphuhoi.oun_de_de.utils.Utils.startOfDayInCambodia;
import com.cdtphuhoi.oun_de_de.common.PaymentTermCycleStatus;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.exceptions.BadRequestException;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.PaymentTermCycleRepository;
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
import com.cdtphuhoi.oun_de_de.services.payment.PaymentTermService;
import com.cdtphuhoi.oun_de_de.services.payment.dto.UpsertPaymentTermData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    private final PaymentTermService paymentTermService;

    private final PaymentTermCycleRepository paymentTermCycleRepository;

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
        if (customer.getPaymentTerm() != null) {
            customer.getPaymentTerm().setStartDate(
                startOfDayInCambodia(customer.getPaymentTerm().getStartDate())
            );
        }
        customer.setReferredBy(referer);
        customer.setWarehouse(warehouse);
        log.info("Creating customer {}", customer.getName());
        var customerDb = customerRepository.save(customer);
        log.info("Created customer, id = {}", customerDb.getId());
        return MapperHelpers.getCustomerMapper().toCustomerResult(customerDb);
    }

    public Page<CustomerResult> findBy(String name, Integer paymentTerm, Pageable pageable) {
        var page = customerRepository.findAll(
            Specification.allOf(
                CustomerSpecifications.containName(name),
                CustomerSpecifications.hasPaymentTerm(paymentTerm)
            ),
            pageable
        );
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
        updatePaymentTermInMemory(customer, updateCustomerData.getPaymentTerm());

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
            .quantity(productSettingDb.getQuantity())
            .build();
    }

    public List<ProductSettingResult> getProductSettings(String customerId) {
        return productSettingRepository.findAllByCustomerId(customerId).stream()
            .map(proSet -> ProductSettingResult.builder()
                .productId(proSet.getProductSettingId().getProductId())
                .customerId(proSet.getProductSettingId().getCustomerId())
                .price(proSet.getPrice())
                .quantity(proSet.getQuantity())
                .build())
            .toList();
    }

    public void updatePaymentTermInMemory(Customer customer, UpsertPaymentTermData upsertPaymentTermData) {
        if (upsertPaymentTermData == null) {
            return;
        }
        customer.setPaymentTerm(
            Optional.ofNullable(customer.getPaymentTerm())
                .orElse(
                    MapperHelpers.getPaymentMapper().createOrgManagedPaymentTerm(
                        upsertPaymentTermData,
                        customer
                    )
                )
        );
        MapperHelpers.getPaymentMapper().updatePaymentTerm(customer.getPaymentTerm(), upsertPaymentTermData);
        var newStartDay = startOfDayInCambodia(upsertPaymentTermData.getStartDate());
        customer.getPaymentTerm().setStartDate(newStartDay);

        updatePaymentTermCycle(customer.getId(), upsertPaymentTermData);
    }

    private void updatePaymentTermCycle(
        String customerId,
        UpsertPaymentTermData upsertPaymentTermData
    ) {
        var activePaymentTermCycle = paymentTermService.getActiveCurrentCycle(customerId);
        if (activePaymentTermCycle == null) {
            return;
        }

        var newStartDay = startOfDayInCambodia(upsertPaymentTermData.getStartDate());
        if (newStartDay.isAfter(activePaymentTermCycle.getStartDate())) {
            throw new BadRequestException("Start date of new term must be before or equal current start date cycle");
        }

        activePaymentTermCycle.setStartDate(newStartDay);
        // if start in 13th, duration 10, should end in 23:59:59 22nd
        var endDate = endOfDayInCambodia(newStartDay.plusDays(upsertPaymentTermData.getDuration() - 1));
        if (endDate.isBefore(cambodiaNow())) {
            if (activePaymentTermCycle.getTotalPaidAmount().equals(activePaymentTermCycle.getTotalAmount())) {
                activePaymentTermCycle.setStatus(PaymentTermCycleStatus.CLOSED);
            } else {
                activePaymentTermCycle.setStatus(PaymentTermCycleStatus.OVERDUE);
            }
        }
        activePaymentTermCycle.setEndDate(endDate);
        paymentTermCycleRepository.save(activePaymentTermCycle);
    }
}
