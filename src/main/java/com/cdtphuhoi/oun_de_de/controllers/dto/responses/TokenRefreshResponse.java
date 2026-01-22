package com.cdtphuhoi.oun_de_de.controllers.dto.responses;

import static com.cdtphuhoi.oun_de_de.common.Constants.BEARER_TOKEN_TYPE;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = BEARER_TOKEN_TYPE;
}
