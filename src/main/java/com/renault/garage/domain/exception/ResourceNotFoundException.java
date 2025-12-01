package com.renault.garage.domain.exception;

/**
 * Exception levée lorsqu'une ressource n'est pas trouvée.
 * 
 * Cette exception est utilisée pour signaler qu'une entité
 * recherchée n'existe pas dans la base de données.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public abstract class ResourceNotFoundException extends BusinessException {

    /**
     * Constructeur avec le type de ressource et son identifiant
     * 
     * @param resourceType le type de ressource (ex: "Garage", "Vehicle")
     * @param resourceId l'identifiant de la ressource
     */
    protected ResourceNotFoundException(String resourceType, Object resourceId) {
        super(String.format("%s avec l'ID %s n'a pas été trouvé", resourceType, resourceId));
    }

    /**
     * Constructeur avec message personnalisé
     * 
     * @param message le message d'erreur
     */
    protected ResourceNotFoundException(String message) {
        super(message);
    }
}
