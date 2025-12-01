package com.renault.garage.application.validator;

import com.renault.garage.domain.model.valueobject.OpeningTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

/**
 * Validateur personnalisé pour les horaires d'ouverture.
 * 
 * Vérifie que toutes les plages horaires sont valides (startTime < endTime).
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public class OpeningHoursValidator implements ConstraintValidator<ValidOpeningHours, Map<DayOfWeek, List<OpeningTime>>> {

    @Override
    public boolean isValid(Map<DayOfWeek, List<OpeningTime>> openingHours, ConstraintValidatorContext context) {
        if (openingHours == null || openingHours.isEmpty()) {
            return false;
        }

        for (Map.Entry<DayOfWeek, List<OpeningTime>> entry : openingHours.entrySet()) {
            List<OpeningTime> times = entry.getValue();
            
            if (times == null || times.isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                    "Les horaires pour " + entry.getKey() + " ne peuvent pas être vides"
                ).addConstraintViolation();
                return false;
            }

            for (OpeningTime time : times) {
                if (!time.isValid()) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                        "Horaires invalides pour " + entry.getKey() + ": " + time
                    ).addConstraintViolation();
                    return false;
                }
            }
        }

        return true;
    }
}
