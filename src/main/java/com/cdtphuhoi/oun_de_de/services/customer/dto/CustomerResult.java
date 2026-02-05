package com.cdtphuhoi.oun_de_de.services.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CustomerResult {
    private String id;

    private Date registerDate;

    private String code;

    private String name;

    private Boolean status;

    // TODO: CodeList enhancement
    private String defaultPrice;

    // TODO: CodeList enhancement
    private String warehouse;

    private String memo;

    private String profileUrl;

    private String shopBannerUrl;

    private String referredBy;
}
