package com.cdtphuhoi.oun_de_de.controllers.dto.coupon;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

@Data
public class CreateWeightRecordRequest {

    private String productName;

    private String unit;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal pricePerProduct;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal quantityPerProduct;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal quantity;

    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal weight;

    private Date outTime;

    private boolean isManual;

    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String memo;
}
