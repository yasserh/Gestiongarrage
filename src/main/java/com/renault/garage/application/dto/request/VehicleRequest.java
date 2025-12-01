package com.renault.garage.application.dto.request;

import com.renault.garage.domain.model.enums.FuelType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de requête pour la création ou modification d'un véhicule.
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
public class VehicleRequest {

    @NotBlank(message = "La marque du véhicule est obligatoire")
    @Size(min = 2, max = 50, message = "La marque doit contenir entre 2 et 50 caractères")
    private String brand;

    @NotBlank(message = "Le modèle du véhicule est obligatoire")
    @Size(min = 1, max = 50, message = "Le modèle doit contenir entre 1 et 50 caractères")
    private String model;

    @NotNull(message = "L'année de fabrication est obligatoire")
    @Min(value = 1900, message = "L'année de fabrication doit être supérieure ou égale à 1900")
    @Max(value = 2100, message = "L'année de fabrication doit être inférieure ou égale à 2100")
    private Integer yearOfManufacture;

    @NotNull(message = "Le type de carburant est obligatoire")
    private FuelType fuelType;

    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Le VIN doit contenir exactement 17 caractères alphanumériques")
    private String vin;

    @Size(max = 30, message = "La couleur ne peut pas dépasser 30 caractères")
    private String color;

    @Min(value = 0, message = "Le kilométrage ne peut pas être négatif")
    private Integer mileage;
}
