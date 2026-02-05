package com.cdtphuhoi.oun_de_de.utils.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateCustomerRequest;
import com.cdtphuhoi.oun_de_de.entities.Contact;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerResult;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = MapperHelpers.class
)
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Customer toCustomer(CreateCustomerData request, User employeeUser);

    @AfterMapping
    default void afterMapping(
        @MappingTarget Customer customer,
        CreateCustomerData request,
        User employeeUser
    ) {
        customer.setEmployee(employeeUser);
        customer.setContact(toContact(request, employeeUser));
        customer.setVehicles(
            MapperHelpers.getVehicleMapper().toListVehicles(request.getVehicles(), customer)
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Contact toContact(CreateCustomerData request, User employeeUser);

    CreateCustomerData toCreateCustomerData(CreateCustomerRequest request);

    CustomerResult toCustomerResult(Customer customer);
}
