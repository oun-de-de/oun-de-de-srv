package com.cdtphuhoi.oun_de_de.controllers.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeResponse {
    private String id;

    private String username;

    private String firstName;

    private String lastName;
}
