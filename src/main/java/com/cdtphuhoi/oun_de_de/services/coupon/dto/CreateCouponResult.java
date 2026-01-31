package com.cdtphuhoi.oun_de_de.services.coupon.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class CreateCouponResult {
    private String id;

    private Date date;

    private String driverName;

    private String remark;

    private CouponVehicleResult vehicle;

    private CouponEmployeeResult employee;

    private List<CouponWeightRecordResponse> weightRecords;
}
