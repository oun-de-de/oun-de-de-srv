package com.cdtphuhoi.oun_de_de.controllers.dto.responses;

import static com.cdtphuhoi.oun_de_de.common.Constants.BEARER_TOKEN_TYPE;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class JwtResponse {
    private String token;
    private String type = BEARER_TOKEN_TYPE;
    private String refreshToken;
    private Long id;
    private String username;
    private List<String> roles;
}
