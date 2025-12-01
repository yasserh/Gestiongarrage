package com.renault.garage.domain.model.enums;

/**
 * Énumération des types de carburant supportés par les véhicules.
 * 
 * Cette enum fait partie du modèle de domaine et représente
 * les différents types de carburant disponibles dans le réseau Renault.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public enum FuelType {
    /**
     * Véhicule à essence
     */
    ESSENCE("Essence"),
    
    /**
     * Véhicule diesel
     */
    DIESEL("Diesel"),
    
    /**
     * Véhicule électrique
     */
    ELECTRIQUE("Électrique"),
    
    /**
     * Véhicule hybride (essence/électrique ou diesel/électrique)
     */
    HYBRIDE("Hybride"),
    
    /**
     * Véhicule GPL (Gaz de Pétrole Liquéfié)
     */
    GPL("GPL");

    private final String displayName;

    FuelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
