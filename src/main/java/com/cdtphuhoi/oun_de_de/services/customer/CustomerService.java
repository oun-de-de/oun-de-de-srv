package com.cdtphuhoi.oun_de_de.services.customer;

import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerResult;
import com.cdtphuhoi.oun_de_de.utils.mappers.MapperHelpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService implements OrgManagementService {

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

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
        var customer = MapperHelpers.getCustomerMapper().toCustomer(createCustomerData, employee);
        customer.setReferredBy(referer);
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
}
