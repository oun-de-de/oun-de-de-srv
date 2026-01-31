package com.cdtphuhoi.oun_de_de.controllers.dto.coupon;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.validation.constraints.DecimalMin;

@Data
public class CreateWeightRecordRequest {

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal price;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal weight;

    private Date outTime;

    private boolean isManual;
}
