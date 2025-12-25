package com.renault.garage.application.dto.response;

import com.renault.garage.domain.model.valueobject.OpeningTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO de réponse pour un garage.
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
public class GarageResponse {

    private Long id;
    private String name;
    private String address;
    private String telephone;
    private String email;
    private Map<DayOfWeek, String> openingHours;
    private Integer vehicleCount;
    private Integer availableCapacity;
    private Boolean isFull;
    // Nouveau champ pour la liste des véhicules (sera null par défaut pour les autres endpoints)
    private List<VehicleResponse> vehicles;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
