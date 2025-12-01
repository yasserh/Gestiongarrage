package com.renault.garage.application.dto.response;

import com.renault.garage.domain.model.enums.FuelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de réponse pour un véhicule.
 * 
 * Pattern utilisé: DTO Pattern
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {

    private Long id;
    private String brand;
    private String model;
    private Integer yearOfManufacture;
    private FuelType fuelType;
    private String vin;
    private String color;
    private Integer mileage;
    private Long garageId;
    private String garageName;
    private Integer accessoryCount;
    private Boolean isEcoFriendly;
    private String displayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
