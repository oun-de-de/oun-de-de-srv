package com.cdtphuhoi.oun_de_de.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackages = {
    "com.cdtphuhoi.oun_de_de.configs",
})
@Import({
    JwtConfig.class,
    WebSecurityConfig.class
})
public class ApplicationConfig {
}
