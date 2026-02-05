package com.cdtphuhoi.oun_de_de.utils.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.cdtphuhoi.oun_de_de.common.VehicleType;
import com.cdtphuhoi.oun_de_de.controllers.dto.customer.CreateVehicleRequest;
import com.cdtphuhoi.oun_de_de.entities.Customer;
import com.cdtphuhoi.oun_de_de.entities.Vehicle;
import com.cdtphuhoi.oun_de_de.services.vehicle.dto.CreateVehicleData;
import org.junit.jupiter.api.Test;
import java.util.List;

class VehicleMapperTest {
    private final VehicleMapper mapper = MapperHelpers.getVehicleMapper();

    @Test
    void testToVehicleResult() {
        var vehicle = new Vehicle();
        var result = mapper.toVehicleResult(vehicle);
        assertNotNull(result);
    }

    @Test
    void testToListVehicleResult() {
        var vehicle = new Vehicle();
        var results = mapper.toListVehicleResult(List.of(vehicle));
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void testToCreateVehicleData() {
        var request = new CreateVehicleRequest();
        var data = mapper.toCreateVehicleData(request);
        assertNotNull(data);
    }

    @Test
    void testStringToVehicleType() {
        assertEquals(VehicleType.TRUCK, mapper.stringToVehicleType("truck"));
        assertEquals(VehicleType.TUK_TUK, mapper.stringToVehicleType("tuk_tuk"));
        assertEquals(VehicleType.OTHERS, mapper.stringToVehicleType("others"));
    }

    @Test
    void testToVehicle() {
        var data = CreateVehicleData.builder().build();
        var customer = new Customer();
        customer.setOrgId("org123");
        var vehicle = mapper.toVehicle(data, customer);
        assertNotNull(vehicle);
        assertEquals(customer.getOrgId(), vehicle.getOrgId());
        assertEquals(customer, vehicle.getCustomer());
    }

    @Test
    void testAfterMappingSetsCustomer() {
        var data = CreateVehicleData.builder().build();
        var customer = new Customer();
        var vehicle = mapper.toVehicle(data, customer);
        assertEquals(customer, vehicle.getCustomer());
    }

    @Test
    void testToListVehicles() {
        var data = CreateVehicleData.builder().build();
        var customer = new Customer();
        var vehicles = mapper.toListVehicles(List.of(data), customer);
        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        assertEquals(customer, vehicles.get(0).getCustomer());
    }

    @Test
    void testToListVehiclesNullInput() {
        assertNull(mapper.toListVehicles(null, null));
    }
}