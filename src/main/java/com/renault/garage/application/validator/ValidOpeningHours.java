package com.renault.garage.application.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation de validation pour les horaires d'ouverture.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OpeningHoursValidator.class)
@Documented
public @interface ValidOpeningHours {
    
    String message() default "Les horaires d'ouverture ne sont pas valides";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
