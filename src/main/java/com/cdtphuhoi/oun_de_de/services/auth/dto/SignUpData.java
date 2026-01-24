package com.cdtphuhoi.oun_de_de.services.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpData {

    private String username;

    private String password;

    private String reEnteredPassword;
}
