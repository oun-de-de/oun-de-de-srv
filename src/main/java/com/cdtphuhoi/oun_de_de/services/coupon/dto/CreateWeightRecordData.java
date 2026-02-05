package com.cdtphuhoi.oun_de_de.services.coupon.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class CreateWeightRecordData {
    private BigDecimal pricePerProduct;

    private BigDecimal weightPerProduct;

    private BigDecimal weight;

    private BigDecimal quantity;

    private Date outTime;

    private boolean isManual;
}
