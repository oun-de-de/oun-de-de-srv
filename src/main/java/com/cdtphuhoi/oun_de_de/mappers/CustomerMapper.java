package com.cdtphuhoi.oun_de_de.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateCustomerRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.UpdateCustomerRequest;
import com.cdtphuhoi.oun_de_de.entities.Contact;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CustomerResult;
import com.cdtphuhoi.oun_de_de.services.customer.dto.UpdateCustomerData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    uses = {VehicleMapper.class},
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
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

    void updateCustomerInternal(
        @MappingTarget Customer customer,
        UpdateCustomerData updateCustomerData
    );

    default void updateCustomer(
        @MappingTarget Customer customer,
        UpdateCustomerData updateCustomerData
    ) {
        updateCustomerInternal(customer, updateCustomerData);

        if (customer.getContact() == null) {
            return;
        }

        updateContact(customer.getContact(), updateCustomerData);
    }

    void updateContact(@MappingTarget Contact contact, UpdateCustomerData updateCustomerData);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Contact toContact(CreateCustomerData request, User employeeUser);

    CreateCustomerData toCreateCustomerData(CreateCustomerRequest request);

    @Mapping(target = "referredBy", source = "customer.referredBy.name")
    CustomerResult toCustomerResult(Customer customer);

    UpdateCustomerData toUpdateCustomerData(UpdateCustomerRequest request);
}
