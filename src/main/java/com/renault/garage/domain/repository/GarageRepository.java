package com.renault.garage.domain.repository;

import com.renault.garage.domain.model.Garage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité Garage.
 * 
 * Fournit les opérations CRUD de base ainsi que des méthodes
 * de recherche personnalisées pour les garages.
 * 
 * Pattern utilisé: Repository Pattern
 * Principe SOLID: Dependency Inversion Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Repository
public interface GarageRepository extends JpaRepository<Garage, Long>, JpaSpecificationExecutor<Garage> {

    /**
     * Recherche un garage par son email.
     * 
     * @param email l'email du garage
     * @return Optional contenant le garage si trouvé
     */
    Optional<Garage> findByEmail(String email);

    /**
     * Recherche des garages par nom (recherche partielle, insensible à la casse).
     * 
     * @param name le nom du garage
     * @param pageable les informations de pagination
     * @return page de garages correspondants
     */
    Page<Garage> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Recherche des garages par ville dans l'adresse.
     * 
     * @param city la ville
     * @param pageable les informations de pagination
     * @return page de garages correspondants
     */
    Page<Garage> findByAddressContainingIgnoreCase(String city, Pageable pageable);

    /**
     * Vérifie si un garage existe avec l'email donné.
     * 
     * @param email l'email à vérifier
     * @return true si un garage existe avec cet email
     */
    boolean existsByEmail(String email);

    /**
     * Compte le nombre de garages ayant au moins un véhicule.
     * 
     * @return le nombre de garages non vides
     */
    @Query("SELECT COUNT(DISTINCT g) FROM Garage g WHERE SIZE(g.vehicles) > 0")
    long countGaragesWithVehicles();

    /**
     * Recherche les garages ayant de la capacité disponible.
     * 
     * @param maxVehicles le nombre maximum de véhicules par garage
     * @param pageable les informations de pagination
     * @return page de garages ayant de la capacité
     */
    @Query("SELECT g FROM Garage g WHERE SIZE(g.vehicles) < :maxVehicles")
    Page<Garage> findGaragesWithAvailableCapacity(@Param("maxVehicles") int maxVehicles, Pageable pageable);

    /**
     * Recherche les garages pleins (quota atteint).
     * 
     * @param maxVehicles le nombre maximum de véhicules par garage
     * @param pageable les informations de pagination
     * @return page de garages pleins
     */
    @Query("SELECT g FROM Garage g WHERE SIZE(g.vehicles) >= :maxVehicles")
    Page<Garage> findFullGarages(@Param("maxVehicles") int maxVehicles, Pageable pageable);
}
