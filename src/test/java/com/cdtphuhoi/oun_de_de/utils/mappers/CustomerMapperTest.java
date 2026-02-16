package com.cdtphuhoi.oun_de_de.utils.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateCustomerRequest;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.mappers.CustomerMapper;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import org.junit.jupiter.api.Test;

class CustomerMapperTest {
    private final CustomerMapper mapper = MapperHelpers.getCustomerMapper();

    @Test
    void testToCreateCustomerData() {
        var request = new CreateCustomerRequest();
        var data = mapper.toCreateCustomerData(request);
        assertNotNull(data);
    }

    @Test
    void testToCustomer() {
        var data = new CreateCustomerData();
        var employee = new User();
        employee.setOrgId("123");
        var customer = mapper.toCustomer(data, employee);
        assertNotNull(customer);
        assertEquals(employee.getOrgId(), customer.getOrgId());
        assertEquals(employee, customer.getEmployee());
    }

    @Test
    void testAfterMappingSetsContactAndVehicles() {
        var data = new CreateCustomerData();
        var employee = new User();
        employee.setOrgId("456");
        var customer = mapper.toCustomer(data, employee);
        assertNotNull(customer.getContact());
        assertEquals(employee.getOrgId(), customer.getOrgId());
    }

    @Test
    void testToContact() {
        var data = new CreateCustomerData();
        var contact = mapper.toContact(data);
        assertNotNull(contact);
    }

    @Test
    void testToCustomerResult() {
        var customer = new Customer();
        var result = mapper.toCustomerResult(customer);
        assertNotNull(result);
    }
}