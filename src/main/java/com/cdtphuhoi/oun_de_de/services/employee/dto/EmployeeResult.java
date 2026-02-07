package com.cdtphuhoi.oun_de_de.services.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmployeeResult {
    private String id;

    private String username;

    private String firstName;

    private String lastName;
}
