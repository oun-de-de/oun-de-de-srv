package com.cdtphuhoi.oun_de_de.services.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerReference {
    private String id;

    private String name;
}
