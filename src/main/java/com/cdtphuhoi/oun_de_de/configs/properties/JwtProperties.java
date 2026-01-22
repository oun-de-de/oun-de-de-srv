package com.cdtphuhoi.oun_de_de.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    @NotBlank
    private String secret;

    @NotNull
    private Duration expiration;

    @NotNull
    private Duration refreshExpiration;
}
