package com.renault.garage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Point d'entrée principal de l'application Renault Garage Management.
 * 
 * Cette application microservice gère les garages, véhicules et accessoires
 * du réseau Renault avec une architecture Clean Architecture et DDD.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableKafka

public class GarageManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(GarageManagementApplication.class, args);
    }
}
