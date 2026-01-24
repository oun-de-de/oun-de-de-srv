package com.cdtphuhoi.oun_de_de.utils.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateCustomerRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CustomerRequestResponse;
import com.cdtphuhoi.oun_de_de.entities.Contact;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    Customer toCustomer(CreateCustomerData request, User employeeUser);

    @AfterMapping
    default void afterMapping(
        @MappingTarget Customer customer,
        CreateCustomerData request,
        User employeeUser) {
        customer.setEmployee(employeeUser);
        customer.setContact(toContact(request));
    }

    Contact toContact(CreateCustomerData request);

    CreateCustomerData toCreateCustomerData(CreateCustomerRequest request);

    CustomerRequestResponse toCustomerRequestResponse(Customer customer);

    List<CustomerRequestResponse> toListCustomerRequestResponse(List<Customer> customers);
}
