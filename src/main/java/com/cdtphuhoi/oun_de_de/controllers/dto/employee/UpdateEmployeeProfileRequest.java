package com.cdtphuhoi.oun_de_de.controllers.dto.employee;

import static com.cdtphuhoi.oun_de_de.common.Constants.DEFAULT_STRING_FIELD_LENGTH;
import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UpdateEmployeeProfileRequest {
    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String firstName;

    @Size(max = DEFAULT_STRING_FIELD_LENGTH)
    private String lastName;
}
