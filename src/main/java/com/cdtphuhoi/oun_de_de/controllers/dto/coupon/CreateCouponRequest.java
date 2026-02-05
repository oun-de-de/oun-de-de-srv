package com.cdtphuhoi.oun_de_de.controllers.dto.coupon;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateCouponRequest {

    private Date date;

    /*
     * Vehicle
     */
    @NotNull
    private UUID vehicleId;

    private String driverName;

    /*
     * USER
     */
    @NotNull
    private UUID employeeId;

    private String remark;

    /*
     * WeightRecord
     */
    @Valid
    private List<CreateWeightRecordRequest> weightRecords;

    /*
     * these fields for weighing software synchronization
     */
    private Long couponNo;

    private Long couponId;

    private String accNo;

    private String delAccNo;

    private Date delDate;
}
