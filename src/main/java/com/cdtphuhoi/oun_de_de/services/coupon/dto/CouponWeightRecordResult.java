package com.cdtphuhoi.oun_de_de.services.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CouponWeightRecordResult {
    private String id;

    private String productName;

    private String unit;

    private BigDecimal pricePerProduct;

    private BigDecimal quantityPerProduct;

    private BigDecimal quantity;

    private BigDecimal weight;

    private BigDecimal amount;

    private LocalDateTime outTime;

    private boolean isManual;

    private String memo;
}
