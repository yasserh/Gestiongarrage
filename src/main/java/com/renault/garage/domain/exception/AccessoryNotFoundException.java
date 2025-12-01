package com.renault.garage.domain.exception;

/**
 * Exception levée lorsqu'un accessoire n'est pas trouvé.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public class AccessoryNotFoundException extends ResourceNotFoundException {

    public AccessoryNotFoundException(Long accessoryId) {
        super("Accessoire", accessoryId);
    }

    public AccessoryNotFoundException(String message) {
        super(message);
    }
}
