package com.cdtphuhoi.oun_de_de.services.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContactResult {
    private String id;

    private String telephone;

    private String email;

    // TODO: need to confirm
    private String geography;

    private String address;

    // TODO: need to confirm
    private String location;

    private String map;

    private String billingAddress;

    private String deliveryAddress;
}
