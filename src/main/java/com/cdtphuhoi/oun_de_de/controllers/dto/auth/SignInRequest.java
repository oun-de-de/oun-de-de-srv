package com.cdtphuhoi.oun_de_de.controllers.dto.auth;

import static com.cdtphuhoi.oun_de_de.common.Constants.PASSWORD_ERROR_MSG;
import static com.cdtphuhoi.oun_de_de.common.Constants.PASSWORD_REGEX;
import static com.cdtphuhoi.oun_de_de.common.Constants.USERNAME_ERROR_MSG;
import static com.cdtphuhoi.oun_de_de.common.Constants.USERNAME_REGEX;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class SignInRequest {
    @NotBlank
    @Pattern(
        regexp = USERNAME_REGEX,
        message = USERNAME_ERROR_MSG
    )
    private String username;

    @NotBlank
    @Pattern(
        regexp = PASSWORD_REGEX,
        message = PASSWORD_ERROR_MSG
    )
    private String password;
}
