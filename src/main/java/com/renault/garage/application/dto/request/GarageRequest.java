package com.renault.garage.application.dto.request;

import com.renault.garage.application.validator.ValidOpeningHours;
import com.renault.garage.domain.model.valueobject.OpeningTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * DTO de requête pour la création ou modification d'un garage.
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
public class GarageRequest {

    @NotBlank(message = "Le nom du garage est obligatoire")
    @Size(min = 3, max = 100, message = "Le nom doit contenir entre 3 et 100 caractères")
    private String name;

    @NotBlank(message = "L'adresse du garage est obligatoire")
    @Size(min = 10, max = 255, message = "L'adresse doit contenir entre 10 et 255 caractères")
    private String address;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Le numéro de téléphone doit contenir entre 10 et 15 chiffres")
    private String telephone;

    @NotBlank(message = "L'email du garage est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotNull(message = "Les horaires d'ouverture sont obligatoires")
    private Map<DayOfWeek, String> openingHours;
}
