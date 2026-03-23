package com.cdtphuhoi.oun_de_de.services.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCouponData {
    private LocalDateTime date;

    private String driverName;

    private String remark;
    /*
     * Vehicle
     */
    private String vehicleId;

    /*
     * CUSTOMER
     */
    private String customerId;

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
}
