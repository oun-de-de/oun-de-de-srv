package com.cdtphuhoi.oun_de_de.configs;

import com.cdtphuhoi.oun_de_de.configs.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class JwtConfig {
}
