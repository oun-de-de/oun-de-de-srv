package com.cdtphuhoi.oun_de_de.services.customer.dto;

import com.cdtphuhoi.oun_de_de.services.vehicle.dto.VehicleResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CustomerResult {
    private String id;

    private LocalDateTime registerDate;

    private String code;

    private String name;

    private Boolean status;

    private String memo;

    private String profileUrl;

    private String shopBannerUrl;

    private String referredBy;

    private PaymentTermResult paymentTerm;

    private List<VehicleResult> vehicles;
}
