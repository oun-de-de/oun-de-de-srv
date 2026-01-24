package com.cdtphuhoi.oun_de_de.services.customer.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class CreateCustomerData {
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

    // TODO: need to confirm
    private String profileUrl;

    // TODO: need to confirm
    private String shopBannerUrl;

    private String employeeId;

    private String telephone;

    private String email;

    private String geography;

    private String address;

    private String location;

    private String map;

    private String billingAddress;

    private String deliveryAddress;
}
