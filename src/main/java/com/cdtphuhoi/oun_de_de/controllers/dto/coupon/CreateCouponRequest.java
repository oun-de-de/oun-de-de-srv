package com.cdtphuhoi.oun_de_de.controllers.dto.coupon;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CreateCouponRequest {

    private LocalDateTime date;

    /*
     * Vehicle
     */
    private UUID vehicleId;

    /*
     * CUSTOMER
     */
    private UUID customerId;

    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String driverName;

    /*
     * USER
     */
    @NotNull
    private UUID employeeId;

    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String remark;

    /*
     * WeightRecord
     */
    @NotNull
    @NotEmpty
    @Valid
    private List<CreateWeightRecordRequest> weightRecords;

    /*
     * these fields for weighing software synchronization
     */
    @NotNull
    private Long couponNo;

    private Long couponId;

    private String accNo;
}
