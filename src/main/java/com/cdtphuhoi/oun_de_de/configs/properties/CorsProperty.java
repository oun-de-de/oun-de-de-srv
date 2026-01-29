package com.cdtphuhoi.oun_de_de.configs.properties;

import lombok.Data;

@Data
public class CorsProperty {
    private String allowedOrigins;
    private String allowedMethods;
    private String allowedHeaders;
    private boolean allowCredentials;
    private int maxAge;
}
