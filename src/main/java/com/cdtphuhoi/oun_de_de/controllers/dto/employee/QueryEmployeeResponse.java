package com.cdtphuhoi.oun_de_de.controllers.dto.employee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QueryEmployeeResponse {
    private String id;

    private String username;

    private String firstName;

    private String lastName;
}
