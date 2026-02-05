package com.cdtphuhoi.oun_de_de.utils.mappers;

import com.cdtphuhoi.oun_de_de.common.VehicleType;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateVehicleRequest;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Vehicle;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.CreateVehicleData;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.VehicleResult;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface VehicleMapper {
    VehicleMapper INSTANCE = Mappers.getMapper(VehicleMapper.class);

    VehicleResult toVehicleResult(Vehicle vehicle);

    List<VehicleResult> toListVehicleResult(List<Vehicle> vehicle);

    CreateVehicleData toCreateVehicleData(CreateVehicleRequest request);

    @ValueMapping(target = "TRUCK", source = "truck")
    @ValueMapping(target = "TUK_TUK", source = "tuk_tuk")
    @ValueMapping(target = "OTHERS", source = "others")
    VehicleType stringToVehicleType(String source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "customer.orgId")
    Vehicle toVehicle(CreateVehicleData createVehicleData, Customer customer);

    @AfterMapping
    default void afterMapping(
        @MappingTarget Vehicle vehicle,
        Customer customer
    ) {
        vehicle.setCustomer(customer);
    }

    default List<Vehicle> toListVehicles(
        List<CreateVehicleData> vehicleDataList,
        Customer customer
    ) {
        if (vehicleDataList == null || customer == null) {
            return null;
        }

        var list = new ArrayList<Vehicle>(vehicleDataList.size());
        for (var createVehicleData : vehicleDataList) {
            list.add(toVehicle(createVehicleData, customer));
        }

        return list;
    }
}
