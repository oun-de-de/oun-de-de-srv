package com.cdtphuhoi.oun_de_de.controllers.dto.coupon;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeleteCouponRequest {
    private String delAccNo;

    private LocalDateTime delDate;
}
