package com.renault.garage.domain.repository.specification;

import com.renault.garage.domain.model.Garage;
import com.renault.garage.domain.model.Vehicle;
import com.renault.garage.domain.model.enums.AccessoryType;
import com.renault.garage.domain.model.enums.FuelType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

/**
 * Spécifications JPA pour les requêtes dynamiques sur les garages.
 * 
 * Utilise le Specification Pattern pour construire des requêtes
 * complexes de manière composable et réutilisable.
 * 
 * Pattern utilisé: Specification Pattern
 * Principe SOLID: Open/Closed Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public class GarageSpecifications {

    /**
     * Spécification pour rechercher les garages par nom.
     * 
     * @param name le nom du garage (recherche partielle, insensible à la casse)
     * @return la spécification
     */
    public static Specification<Garage> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("name")),
                "%" + name.toLowerCase() + "%"
            );
        };
    }

    /**
     * Spécification pour rechercher les garages par ville.
     * 
     * @param city la ville (recherche dans l'adresse)
     * @return la spécification
     */
    public static Specification<Garage> hasCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (city == null || city.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("address")),
                "%" + city.toLowerCase() + "%"
            );
        };
    }

    /**
     * Spécification pour rechercher les garages ayant des véhicules d'un type de carburant donné.
     * 
     * @param fuelType le type de carburant
     * @return la spécification
     */
    public static Specification<Garage> hasVehicleWithFuelType(FuelType fuelType) {
        return (root, query, criteriaBuilder) -> {
            if (fuelType == null) {
                return criteriaBuilder.conjunction();
            }
            
            Join<Garage, Vehicle> vehicleJoin = root.join("vehicles", JoinType.INNER);
            query.distinct(true);
            
            return criteriaBuilder.equal(vehicleJoin.get("fuelType"), fuelType);
        };
    }

    /**
     * Spécification pour rechercher les garages ayant au moins un véhicule
     * avec un accessoire d'un type donné.
     * 
     * @param accessoryType le type d'accessoire
     * @return la spécification
     */
    public static Specification<Garage> hasVehicleWithAccessoryType(AccessoryType accessoryType) {
        return (root, query, criteriaBuilder) -> {
            if (accessoryType == null) {
                return criteriaBuilder.conjunction();
            }
            
            Join<Object, Object> vehicleJoin = root.join("vehicles", JoinType.INNER);
            Join<Object, Object> accessoryJoin = vehicleJoin.join("accessories", JoinType.INNER);
            query.distinct(true);
            
            return criteriaBuilder.equal(accessoryJoin.get("type"), accessoryType);
        };
    }

    /**
     * Spécification pour rechercher les garages ayant de la capacité disponible.
     * 
     * @return la spécification
     */
    public static Specification<Garage> hasAvailableCapacity() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.lessThan(
                criteriaBuilder.size(root.get("vehicles")),
                Garage.MAX_VEHICLES_PER_GARAGE
            );
    }

    /**
     * Spécification pour rechercher les garages pleins.
     * 
     * @return la spécification
     */
    public static Specification<Garage> isFull() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThanOrEqualTo(
                criteriaBuilder.size(root.get("vehicles")),
                Garage.MAX_VEHICLES_PER_GARAGE
            );
    }

    /**
     * Spécification pour rechercher les garages ayant au moins un véhicule.
     * 
     * @return la spécification
     */
    public static Specification<Garage> hasVehicles() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.greaterThan(
                criteriaBuilder.size(root.get("vehicles")),
                0
            );
    }

    /**
     * Spécification pour rechercher les garages vides.
     * 
     * @return la spécification
     */
    public static Specification<Garage> isEmpty() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(
                criteriaBuilder.size(root.get("vehicles")),
                0
            );
    }

    /**
     * Spécification pour rechercher les garages par email.
     * 
     * @param email l'email du garage
     * @return la spécification
     */
    public static Specification<Garage> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(
                criteriaBuilder.lower(root.get("email")),
                email.toLowerCase()
            );
        };
    }
}
