package com.renault.garage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // Cette classe permet d'isoler la configuration JPA
    // pour qu'elle ne perturbe pas les tests @WebMvcTest
}
