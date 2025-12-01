package com.renault.garage.domain.model.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Value Object représentant une plage horaire d'ouverture.
 * 
 * Ce Value Object est immuable et représente une période d'ouverture
 * avec une heure de début et une heure de fin.
 * 
 * Pattern utilisé: Value Object (DDD)
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpeningTime {

    /**
     * Heure de début de la plage horaire
     */
    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime startTime;

    /**
     * Heure de fin de la plage horaire
     */
    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime endTime;

    /**
     * Valide que l'heure de début est avant l'heure de fin.
     * 
     * @return true si la plage horaire est valide
     */
    public boolean isValid() {
        if (startTime == null || endTime == null) {
            return false;
        }
        return startTime.isBefore(endTime);
    }

    /**
     * Vérifie si une heure donnée se trouve dans cette plage horaire.
     * 
     * @param time l'heure à vérifier
     * @return true si l'heure est dans la plage
     */
    public boolean contains(LocalTime time) {
        if (time == null || !isValid()) {
            return false;
        }
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", startTime, endTime);
    }
}
