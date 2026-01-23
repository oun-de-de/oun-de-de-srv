package com.cdtphuhoi.oun_de_de.controllers.dto.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class JwtResponse {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    private String username;

    private String type;

    private List<String> roles;
}
