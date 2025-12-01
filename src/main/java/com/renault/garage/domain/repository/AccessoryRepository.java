package com.renault.garage.domain.repository;

import com.renault.garage.domain.model.Accessory;
import com.renault.garage.domain.model.enums.AccessoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository pour l'entité Accessory.
 * 
 * Fournit les opérations CRUD de base ainsi que des méthodes
 * de recherche personnalisées pour les accessoires.
 * 
 * Pattern utilisé: Repository Pattern
 * Principe SOLID: Dependency Inversion Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long>, JpaSpecificationExecutor<Accessory> {

    /**
     * Recherche tous les accessoires d'un véhicule spécifique.
     * 
     * @param vehicleId l'ID du véhicule
     * @param pageable les informations de pagination
     * @return page d'accessoires du véhicule
     */
    Page<Accessory> findByVehicleId(Long vehicleId, Pageable pageable);

    /**
     * Recherche tous les accessoires d'un véhicule (liste complète).
     * 
     * @param vehicleId l'ID du véhicule
     * @return liste d'accessoires du véhicule
     */
    List<Accessory> findByVehicleId(Long vehicleId);

    /**
     * Recherche des accessoires par type.
     * 
     * @param type le type d'accessoire
     * @param pageable les informations de pagination
     * @return page d'accessoires du type
     */
    Page<Accessory> findByType(AccessoryType type, Pageable pageable);

    /**
     * Recherche des accessoires par nom (recherche partielle).
     * 
     * @param name le nom de l'accessoire
     * @param pageable les informations de pagination
     * @return page d'accessoires correspondants
     */
    Page<Accessory> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Recherche des accessoires dans une fourchette de prix.
     * 
     * @param minPrice le prix minimum
     * @param maxPrice le prix maximum
     * @param pageable les informations de pagination
     * @return page d'accessoires dans la fourchette
     */
    Page<Accessory> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Compte le nombre d'accessoires d'un véhicule.
     * 
     * @param vehicleId l'ID du véhicule
     * @return le nombre d'accessoires
     */
    long countByVehicleId(Long vehicleId);

    /**
     * Recherche les accessoires par type pour un véhicule spécifique.
     * 
     * @param vehicleId l'ID du véhicule
     * @param type le type d'accessoire
     * @return liste d'accessoires correspondants
     */
    List<Accessory> findByVehicleIdAndType(Long vehicleId, AccessoryType type);

    /**
     * Recherche les accessoires les plus chers.
     * 
     * @param limit le nombre d'accessoires à retourner
     * @return liste des accessoires les plus chers
     */
    @Query("SELECT a FROM Accessory a ORDER BY a.price DESC")
    List<Accessory> findTopExpensiveAccessories(Pageable pageable);

    /**
     * Calcule le prix total des accessoires d'un véhicule.
     * 
     * @param vehicleId l'ID du véhicule
     * @return le prix total
     */
    @Query("SELECT COALESCE(SUM(a.price), 0) FROM Accessory a WHERE a.vehicle.id = :vehicleId")
    BigDecimal calculateTotalPriceByVehicle(@Param("vehicleId") Long vehicleId);

    /**
     * Recherche les garages ayant au moins un véhicule avec un accessoire d'un type donné.
     * 
     * @param type le type d'accessoire
     * @return liste des IDs de garages
     */
    @Query("SELECT DISTINCT a.vehicle.garage.id FROM Accessory a WHERE a.type = :type")
    List<Long> findGarageIdsWithAccessoryType(@Param("type") AccessoryType type);
}
