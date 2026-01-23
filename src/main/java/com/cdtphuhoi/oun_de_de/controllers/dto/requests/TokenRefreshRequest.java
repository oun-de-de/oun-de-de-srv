package com.cdtphuhoi.oun_de_de.controllers.dto.requests;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
public class TokenRefreshRequest {
    @NotBlank
    private String refreshToken;
}
