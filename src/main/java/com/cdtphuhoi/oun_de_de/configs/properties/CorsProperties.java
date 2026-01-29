package com.cdtphuhoi.oun_de_de.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "cors-config")
@Data
public class CorsProperties {
    private CorsProperty properties;
}
