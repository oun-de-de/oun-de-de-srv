package com.cdtphuhoi.oun_de_de.services.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReIssueTokenData {
    private String accessToken;

    private String refreshToken;
}
