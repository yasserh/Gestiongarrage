package com.renault.garage.domain.repository;

import com.renault.garage.domain.model.Vehicle;
import com.renault.garage.domain.model.enums.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Vehicle.
 * 
 * Fournit les opérations CRUD de base ainsi que des méthodes
 * de recherche personnalisées pour les véhicules.
 * 
 * Pattern utilisé: Repository Pattern
 * Principe SOLID: Dependency Inversion Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {

    /**
     * Recherche tous les véhicules d'un garage spécifique.
     * 
     * @param garageId l'ID du garage
     * @param pageable les informations de pagination
     * @return page de véhicules du garage
     */
    Page<Vehicle> findByGarageId(Long garageId, Pageable pageable);

    /**
     * Recherche des véhicules par marque.
     * 
     * @param brand la marque
     * @param pageable les informations de pagination
     * @return page de véhicules de la marque
     */
    Page<Vehicle> findByBrandIgnoreCase(String brand, Pageable pageable);

    /**
     * Recherche des véhicules par modèle.
     * 
     * @param model le modèle
     * @param pageable les informations de pagination
     * @return page de véhicules du modèle
     */
    Page<Vehicle> findByModelIgnoreCase(String model, Pageable pageable);

    /**
     * Recherche des véhicules par type de carburant.
     * 
     * @param fuelType le type de carburant
     * @param pageable les informations de pagination
     * @return page de véhicules du type de carburant
     */
    Page<Vehicle> findByFuelType(FuelType fuelType, Pageable pageable);

    /**
     * Recherche des véhicules par marque et modèle.
     * 
     * @param brand la marque
     * @param model le modèle
     * @param pageable les informations de pagination
     * @return page de véhicules correspondants
     */
    Page<Vehicle> findByBrandIgnoreCaseAndModelIgnoreCase(String brand, String model, Pageable pageable);

    /**
     * Recherche un véhicule par son VIN.
     * 
     * @param vin le numéro VIN
     * @return Optional contenant le véhicule si trouvé
     */
    Optional<Vehicle> findByVin(String vin);

    /**
     * Compte le nombre de véhicules dans un garage.
     * 
     * @param garageId l'ID du garage
     * @return le nombre de véhicules
     */
    long countByGarageId(Long garageId);

    /**
     * Vérifie si un véhicule existe avec le VIN donné.
     * 
     * @param vin le VIN à vérifier
     * @return true si un véhicule existe avec ce VIN
     */
    boolean existsByVin(String vin);

    /**
     * Recherche tous les véhicules d'un modèle donné dans plusieurs garages.
     * 
     * @param model le modèle recherché
     * @return liste de véhicules du modèle
     */
    @Query("SELECT v FROM Vehicle v WHERE LOWER(v.model) = LOWER(:model)")
    List<Vehicle> findAllByModel(@Param("model") String model);

    /**
     * Recherche les véhicules par type de carburant dans un garage spécifique.
     * 
     * @param garageId l'ID du garage
     * @param fuelType le type de carburant
     * @param pageable les informations de pagination
     * @return page de véhicules correspondants
     */
    @Query("SELECT v FROM Vehicle v WHERE v.garage.id = :garageId AND v.fuelType = :fuelType")
    Page<Vehicle> findByGarageIdAndFuelType(
        @Param("garageId") Long garageId,
        @Param("fuelType") FuelType fuelType,
        Pageable pageable
    );

    /**
     * Recherche les véhicules écologiques (électriques ou hybrides).
     * 
     * @param pageable les informations de pagination
     * @return page de véhicules écologiques
     */
    @Query("SELECT v FROM Vehicle v WHERE v.fuelType IN ('ELECTRIQUE', 'HYBRIDE')")
    Page<Vehicle> findEcoFriendlyVehicles(Pageable pageable);
}
