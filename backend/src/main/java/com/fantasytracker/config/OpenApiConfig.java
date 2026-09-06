package com.fantasytracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fantasyTrackerOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fantasy Tracker API")
                        .description("REST API for players, price history and personal tracking")
                        .version("v1"));
    }
}
