package com.renault.garage.domain.exception;

/**
 * Exception levée lorsqu'un garage n'est pas trouvé.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public class GarageNotFoundException extends ResourceNotFoundException {

    public GarageNotFoundException(Long garageId) {
        super("Garage", garageId);
    }

    public GarageNotFoundException(String message) {
        super(message);
    }
}
