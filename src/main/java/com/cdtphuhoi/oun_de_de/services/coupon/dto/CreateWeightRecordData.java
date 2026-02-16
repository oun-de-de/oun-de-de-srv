package com.cdtphuhoi.oun_de_de.services.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateWeightRecordData {
    private String productName;

    private String unit;

    private BigDecimal pricePerProduct;

    private BigDecimal quantityPerProduct;

    private BigDecimal quantity;

    private BigDecimal weight;

    private LocalDateTime outTime;

    private boolean isManual;

    private String memo;
}
