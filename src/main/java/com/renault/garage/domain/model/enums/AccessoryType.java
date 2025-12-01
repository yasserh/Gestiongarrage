package com.renault.garage.domain.model.enums;

/**
 * Énumération des types d'accessoires disponibles pour les véhicules.
 * 
 * Cette enum fait partie du modèle de domaine et catégorise
 * les différents types d'accessoires proposés par Renault.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public enum AccessoryType {
    /**
     * Accessoires intérieurs (sièges, tapis, etc.)
     */
    INTERIEUR("Intérieur"),
    
    /**
     * Accessoires extérieurs (jantes, spoilers, etc.)
     */
    EXTERIEUR("Extérieur"),
    
    /**
     * Accessoires électroniques (GPS, caméra, etc.)
     */
    ELECTRONIQUE("Électronique"),
    
    /**
     * Accessoires de sécurité (alarme, airbags supplémentaires, etc.)
     */
    SECURITE("Sécurité"),
    
    /**
     * Accessoires de confort (climatisation, sièges chauffants, etc.)
     */
    CONFORT("Confort");

    private final String displayName;

    AccessoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
