package com.cdtphuhoi.oun_de_de;

import com.cdtphuhoi.oun_de_de.configs.ApplicationConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(ApplicationConfig.class)
public class OunDeDeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OunDeDeApplication.class, args);
    }
}
