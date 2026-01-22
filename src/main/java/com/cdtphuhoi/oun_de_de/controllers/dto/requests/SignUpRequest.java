package com.cdtphuhoi.oun_de_de.controllers.dto.requests;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class SignUpRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String reEnteredPassword;
}
