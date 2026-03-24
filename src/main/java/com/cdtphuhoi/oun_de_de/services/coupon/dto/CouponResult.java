package com.cdtphuhoi.oun_de_de.services.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CouponResult {
    private String id;

    private LocalDateTime date;

    private String driverName;

    private String remark;

    private CouponVehicleResult vehicle;

    private CouponEmployeeResult employee;

    private String invoiceId;

    private String paymentTermCycleId;

    private String invoiceRefNo;

    private List<CouponWeightRecordResult> weightRecords;

    private String customerName;

    /*
     * these fields for weighing software synchronization
     */
    private Long couponNo;

    private Long couponId;

    private String accNo;

    private String delAccNo;

    private LocalDateTime delDate;
}
