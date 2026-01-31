package com.cdtphuhoi.oun_de_de.services.coupon.dto;

import com.cdtphuhoi.oun_de_de.controllers.dto.coupon.CreateWeightRecordRequest;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class CreateCouponData {
    private Date date;

    private String driverName;

    private String remark;
    /*
     * Vehicle
     */
    private String vehicleId;

    /*
     * USER
     */
    private String employeeId;

    /*
     * WeightRecord
     */
    private List<CreateWeightRecordRequest> weightRecords;
}
