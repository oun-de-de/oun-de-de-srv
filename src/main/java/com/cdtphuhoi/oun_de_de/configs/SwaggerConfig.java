package com.cdtphuhoi.oun_de_de.configs;

import static com.cdtphuhoi.oun_de_de.common.Constants.AUTHORIZATION_HEADER;
import static com.cdtphuhoi.oun_de_de.common.Constants.BEARER_TOKEN_TYPE;
import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = SWAGGER_SECURITY_SCHEME_NAME,
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = BEARER_TOKEN_TYPE
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(
                new Info()
                    .title("Oun De De Swagger API Documentation")
                    .description("Oun De De Swagger API Documentation")
            );
    }
}
