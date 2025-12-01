package com.renault.garage.domain.repository.specification;

import com.renault.garage.domain.model.Vehicle;
import com.renault.garage.domain.model.enums.FuelType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Spécifications JPA pour les requêtes dynamiques sur les véhicules.
 * 
 * Pattern utilisé: Specification Pattern
 * Principe SOLID: Open/Closed Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public class VehicleSpecifications {

    /**
     * Spécification pour rechercher les véhicules par marque.
     * 
     * @param brand la marque
     * @return la spécification
     */
    public static Specification<Vehicle> hasBrand(String brand) {
        return (root, query, criteriaBuilder) -> {
            if (brand == null || brand.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("brand")),
                brand.toLowerCase()
            );
        };
    }

    /**
     * Spécification pour rechercher les véhicules par modèle.
     * 
     * @param model le modèle
     * @return la spécification
     */
    public static Specification<Vehicle> hasModel(String model) {
        return (root, query, criteriaBuilder) -> {
            if (model == null || model.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("model")),
                model.toLowerCase()
            );
        };
    }

    /**
     * Spécification pour rechercher les véhicules par type de carburant.
     * 
     * @param fuelType le type de carburant
     * @return la spécification
     */
    public static Specification<Vehicle> hasFuelType(FuelType fuelType) {
        return (root, query, criteriaBuilder) -> {
            if (fuelType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("fuelType"), fuelType);
        };
    }

    /**
     * Spécification pour rechercher les véhicules par année de fabrication.
     * 
     * @param year l'année de fabrication
     * @return la spécification
     */
    public static Specification<Vehicle> hasYearOfManufacture(Integer year) {
        return (root, query, criteriaBuilder) -> {
            if (year == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("yearOfManufacture"), year);
        };
    }

    /**
     * Spécification pour rechercher les véhicules par garage.
     * 
     * @param garageId l'ID du garage
     * @return la spécification
     */
    public static Specification<Vehicle> belongsToGarage(Long garageId) {
        return (root, query, criteriaBuilder) -> {
            if (garageId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("garage").get("id"), garageId);
        };
    }

    /**
     * Spécification pour rechercher les véhicules écologiques.
     * 
     * @return la spécification
     */
    public static Specification<Vehicle> isEcoFriendly() {
        return (root, query, criteriaBuilder) -> 
            root.get("fuelType").in(FuelType.ELECTRIQUE, FuelType.HYBRIDE);
    }

    /**
     * Spécification pour rechercher les véhicules par couleur.
     * 
     * @param color la couleur
     * @return la spécification
     */
    public static Specification<Vehicle> hasColor(String color) {
        return (root, query, criteriaBuilder) -> {
            if (color == null || color.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("color")),
                color.toLowerCase()
            );
        };
    }
}
