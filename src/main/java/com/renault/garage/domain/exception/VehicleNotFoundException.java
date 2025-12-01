package com.renault.garage.domain.exception;

/**
 * Exception levée lorsqu'un véhicule n'est pas trouvé.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public class VehicleNotFoundException extends ResourceNotFoundException {

    public VehicleNotFoundException(Long vehicleId) {
        super("Véhicule", vehicleId);
    }

    public VehicleNotFoundException(String message) {
        super(message);
    }
}
