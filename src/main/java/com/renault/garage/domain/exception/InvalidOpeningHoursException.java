package com.renault.garage.domain.exception;

/**
 * Exception levée lorsque les horaires d'ouverture sont invalides.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public class InvalidOpeningHoursException extends BusinessException {

    public InvalidOpeningHoursException(String message) {
        super(message);
    }

    public InvalidOpeningHoursException(String day, String reason) {
        super(String.format(
            "Horaires d'ouverture invalides pour %s: %s",
            day,
            reason
        ));
    }
}
