package com.cdtphuhoi.oun_de_de.controllers.dto.requests;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class SignInRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
