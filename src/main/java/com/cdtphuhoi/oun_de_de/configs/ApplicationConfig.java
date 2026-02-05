package com.cdtphuhoi.oun_de_de.configs;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@ComponentScan(basePackages = {
    "com.cdtphuhoi.oun_de_de.configs.properties",
})
@Import({
    WebSecurityConfig.class,
    SwaggerConfig.class
})
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class ApplicationConfig {
}
