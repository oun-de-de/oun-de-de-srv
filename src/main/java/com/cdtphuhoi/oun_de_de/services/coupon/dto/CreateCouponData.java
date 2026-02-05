package com.cdtphuhoi.oun_de_de.services.coupon.dto;

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
    private List<CreateWeightRecordData> weightRecords;

    /*
     * these fields for weighing software synchronization
     */
    private Long couponNo;

    private Long couponId;

    private String accNo;

    private String delAccNo;

    private Date delDate;
}
