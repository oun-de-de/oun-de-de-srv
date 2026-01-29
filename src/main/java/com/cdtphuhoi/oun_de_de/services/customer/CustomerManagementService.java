package com.cdtphuhoi.oun_de_de.services.customer;

import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.OrgManagementService;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import com.cdtphuhoi.oun_de_de.utils.mappers.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerManagementService implements OrgManagementService {

    private final CustomerRepository customerRepository;

    private final UserRepository userRepository;

    public Customer create(CreateCustomerData createCustomerData) {
        var employee = userRepository.findById(createCustomerData.getEmployeeId())
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    String.format("Employee [id=%s] not found", createCustomerData.getEmployeeId())
                )
            );
        var customer = CustomerMapper.INSTANCE.toCustomer(createCustomerData, employee);
        log.info("Creating customer {}", customer.getName());
        var customerDb = customerRepository.save(customer);
        log.info("Created customer, id = {}", customerDb.getId());
        return customerDb;
    }

    @Transactional(readOnly = true)
    public List<Customer> findBy() {
        return StreamSupport
            .stream(
                customerRepository.findAll().spliterator(), false
            )
            .collect(Collectors.toList());
    }

}
