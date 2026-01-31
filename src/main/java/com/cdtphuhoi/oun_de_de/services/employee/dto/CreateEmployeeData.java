package com.cdtphuhoi.oun_de_de.services.employee.dto;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class CreateEmployeeData {

    private String username;

    private String password;

    private String reEnteredPassword;

    private UUID createById;
}
