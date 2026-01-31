package com.cdtphuhoi.oun_de_de.controllers.dto.customer;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class CustomerResponse {
    private String id;

    private Date registerDate;

    private String code;

    private String name;

    private Boolean status;

    // TODO: CodeList enhancement
    private String customerType;

    // TODO: CodeList enhancement
    private String defaultPrice;

    // TODO: CodeList enhancement
    private String warehouse;

    private String memo;

    private String profileUrl;

    private String shopBannerUrl;
}
