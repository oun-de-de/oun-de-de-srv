package com.cdtphuhoi.oun_de_de.utils.mappers;

import com.cdtphuhoi.oun_de_de.common.VehicleType;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateCustomerRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.QueryCustomerResponse;
import com.cdtphuhoi.oun_de_de.entities.Contact;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.entities.Vehicle;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateCustomerData;
import com.cdtphuhoi.oun_de_de.services.customer.dto.CreateVehicleData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.ArrayList;
import java.util.List;

@Mapper
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
        customer.setVehicles(toListVehicles(request.getVehicles(), employeeUser));
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Contact toContact(CreateCustomerData request, User employeeUser);

    CreateCustomerData toCreateCustomerData(CreateCustomerRequest request);

    QueryCustomerResponse toCustomerResponse(Customer customer);

    List<QueryCustomerResponse> toListCustomerResponse(List<Customer> customers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employeeUser.orgId")
    Vehicle toVehicle(CreateVehicleData createVehicleData, User employeeUser);

    default List<Vehicle> toListVehicles(List<CreateVehicleData> vehicleDataList, User employeeUser) {
        if (vehicleDataList == null || employeeUser == null) {
            return null;
        }

        var list = new ArrayList<Vehicle>(vehicleDataList.size());
        for (var createVehicleData : vehicleDataList) {
            list.add(toVehicle(createVehicleData, employeeUser));
        }

        return list;
    }

    @ValueMapping(target = "TRUCK", source = "truck")
    @ValueMapping(target = "TUK_TUK", source = "tuk tuk")
    @ValueMapping(target = "OTHERS", source = "others")
    VehicleType stringToVehicleType(String source);
}
