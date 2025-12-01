package com.renault.garage.domain.exception;

/**
 * Exception levée lorsque le quota maximum de véhicules dans un garage est dépassé.
 * 
 * Contrainte métier: Un garage ne peut stocker plus de 50 véhicules.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public class VehicleQuotaExceededException extends BusinessException {

    private static final int MAX_VEHICLES = 50;

    public VehicleQuotaExceededException(Long garageId) {
        super(String.format(
            "Le garage avec l'ID %d a atteint le quota maximum de %d véhicules",
            garageId,
            MAX_VEHICLES
        ));
    }

    public VehicleQuotaExceededException(String garageName) {
        super(String.format(
            "Le garage '%s' a atteint le quota maximum de %d véhicules",
            garageName,
            MAX_VEHICLES
        ));
    }
}
