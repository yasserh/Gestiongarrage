package com.renault.garage.infrastructure.kafka.event;

import com.renault.garage.domain.model.enums.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Événement Kafka publié lors de la création d'un véhicule.
 * 
 * Pattern utilisé: Event-Driven Architecture, Observer Pattern
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleCreatedEvent {

    private Long vehicleId;
    private String brand;
    private String model;
    private Integer yearOfManufacture;
    private FuelType fuelType;
    private String vin;
    private Long garageId;
    private String garageName;
    private LocalDateTime createdAt;
    private String eventId;
}
