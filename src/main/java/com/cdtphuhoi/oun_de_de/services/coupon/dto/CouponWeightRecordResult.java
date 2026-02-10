package com.cdtphuhoi.oun_de_de.services.coupon.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class CouponWeightRecordResult {
    private String id;

    private String productName;

    private String unit;

    private BigDecimal pricePerProduct;

    private BigDecimal quantityPerProduct;

    private BigDecimal quantity;

    private BigDecimal weight;

    private Date outTime;

    private boolean isManual;

    private String memo;
}
