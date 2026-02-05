package com.cdtphuhoi.oun_de_de.utils.mappers;

import com.cdtphuhoi.oun_de_de.controllers.dto.coupon.CreateCouponRequest;
import com.cdtphuhoi.oun_de_de.entities.Coupon;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.entities.Vehicle;
import com.cdtphuhoi.oun_de_de.entities.WeightRecord;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CouponResult;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateCouponData;
import com.cdtphuhoi.oun_de_de.services.coupon.dto.CreateWeightRecordData;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CouponMapper {
    CouponMapper INSTANCE = Mappers.getMapper(CouponMapper.class);

    CreateCouponData toCreateCouponData(CreateCouponRequest request);

    CouponResult toCouponResult(Coupon coupon);

    List<CouponResult> toListCouponResult(List<Coupon> coupon);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employee.orgId")
    Coupon toCoupon(CreateCouponData createCouponData, User employee, Vehicle vehicle);

    @AfterMapping
    default void afterMapping(
        @MappingTarget Coupon coupon,
        CreateCouponData createCouponData,
        User employee,
        Vehicle vehicle
    ) {
        coupon.setEmployee(employee);
        coupon.setVehicle(vehicle);
        coupon.setWeightRecords(
            toListWeightRecords(
                createCouponData.getWeightRecords(),
                employee,
                coupon)
        );
    }

    default List<WeightRecord> toListWeightRecords(
        List<CreateWeightRecordData> weightRecords,
        User employee,
        Coupon coupon) {
        if (weightRecords == null || employee == null) {
            return null;
        }

        var list = new ArrayList<WeightRecord>(weightRecords.size());
        for (var weightRecord : weightRecords) {
            list.add(toWeightRecord(weightRecord, employee, coupon));
        }

        return list;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orgId", source = "employee.orgId")
    WeightRecord toWeightRecord(CreateWeightRecordData weightRecord, User employee, Coupon coupon);
}
