package com.cdtphuhoi.oun_de_de.services.customer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.cdtphuhoi.oun_de_de.exceptions.ResourceNotFoundException;
import com.cdtphuhoi.oun_de_de.repositories.CustomerRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    void create_shouldThrowWhenEmployeeNotFound() {
        var data = mock(CreateCustomerData.class);
        var mockId = "mockId";
        when(data.getEmployeeId()).thenReturn(mockId);
        when(userRepository.findOneById(mockId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.create(data));
    }
}