package com.cdtphuhoi.oun_de_de.controllers.dto.responses;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class JwtResponse {
    private String userId;
    private String accessToken;
    private String refreshToken;
    private String username;
    private Long expiresIn;
    private String type;
    private List<String> roles;
}
