package com.cdtphuhoi.oun_de_de.services.customer;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerResult;
import com.cdtphuhoi.oun_de_de.mappers.CustomerMapper;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerService customerService;


    @Test
    void create_shouldCreateCustomer() {
        var data = mock(CreateCustomerData.class);
        var mockId = "mockId";
        when(data.getEmployeeId()).thenReturn(mockId);
        var employee = mock(User.class);
        when(userRepository.findOneById(mockId)).thenReturn(Optional.of(employee));
        var customer = mock(Customer.class);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        var result = customerService.create(data);

        assertNotNull(result);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void create_shouldThrowWhenEmployeeNotFound() {
        var data = mock(CreateCustomerData.class);
        var mockId = "mockId";
        when(data.getEmployeeId()).thenReturn(mockId);
        when(userRepository.findOneById(mockId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.create(data));
    }
}