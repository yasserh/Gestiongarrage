package com.renault.garage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration Swagger/OpenAPI pour la documentation de l'API.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Renault Garage Management API")
                .version("1.0.0")
                .description("API REST pour la gestion des garages, véhicules et accessoires Renault")
                .contact(new Contact()
                    .name("Renault Team")
                    .email("contact@renault.com"))
                .license(new License()
                    .name("© 2024 Renault")
                    .url("https://www.renault.com")))
            .components(new Components()
                .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token pour l'authentification")))
            .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
