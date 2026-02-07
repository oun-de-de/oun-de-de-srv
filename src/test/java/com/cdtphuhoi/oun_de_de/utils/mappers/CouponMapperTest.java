package com.cdtphuhoi.oun_de_de.utils.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.cdtphuhoi.oun_de_de.controllers.dto.coupon.CreateCouponRequest;
import com.cdtphuhoi.oun_de_de.entities.Coupon;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.entities.Vehicle;
import com.cdtphuhoi.oun_de_de.mappers.CouponMapper;
import com.cdtphuhoi.oun_de_de.mappers.MapperHelpers;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponData;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateWeightRecordData;
import org.junit.jupiter.api.Test;
import java.util.List;

class CouponMapperTest {

    private final CouponMapper mapper = MapperHelpers.getCouponMapper();

    @Test
    void testToCreateCouponData() {
        var request = new CreateCouponRequest();
        // set fields as needed
        var data = mapper.toCreateCouponData(request);
        assertNotNull(data);
    }

    @Test
    void testToCouponResult() {
        var coupon = new Coupon();
        var result = mapper.toCouponResult(coupon);
        assertNotNull(result);
    }

    @Test
    void testToListCouponResult() {
        var coupon = new Coupon();
        var results = mapper.toListCouponResult(List.of(coupon));
        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    void testToCouponAndAfterMapping() {
        var data = CreateCouponData.builder().build();
        var employee = new User();
        employee.setOrgId("123L");
        var vehicle = new Vehicle();
        var coupon = mapper.toCoupon(data, employee, vehicle);
        assertNotNull(coupon);
        assertEquals(employee, coupon.getEmployee());
        assertEquals(vehicle, coupon.getVehicle());
        assertEquals(employee.getOrgId(), coupon.getOrgId());
    }

    @Test
    void testToListWeightRecords() {
        var wrData = CreateWeightRecordData.builder().build();
        var employee = new User();
        employee.setOrgId("1L");
        var coupon = new Coupon();
        var records = mapper.toListWeightRecords(List.of(wrData), employee, coupon);
        assertNotNull(records);
        assertEquals(1, records.size());
    }

    @Test
    void testToWeightRecord() {
        var wrData = CreateWeightRecordData.builder().build();
        var employee = new User();
        employee.setOrgId("2L");
        var coupon = new Coupon();
        var record = mapper.toWeightRecord(wrData, employee, coupon);
        assertNotNull(record);
        assertEquals(employee.getOrgId(), record.getOrgId());
    }
}