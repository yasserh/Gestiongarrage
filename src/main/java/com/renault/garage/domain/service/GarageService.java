package com.renault.garage.domain.service;

import com.renault.garage.application.dto.request.GarageRequest;
import com.renault.garage.application.dto.response.GarageResponse;
import com.renault.garage.domain.model.enums.AccessoryType;
import com.renault.garage.domain.model.enums.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface du service de gestion des garages.
 * 
 * Pattern utilisé: Service Layer Pattern
 * Principe SOLID: Dependency Inversion Principle, Interface Segregation Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public interface GarageService {

    /**
     * Crée un nouveau garage.
     * 
     * @param request les données du garage
     * @return le garage créé
     */
    GarageResponse createGarage(GarageRequest request);

    /**
     * Récupère un garage par son ID.
     * 
     * @param id l'ID du garage
     * @return le garage trouvé
     */
    GarageResponse getGarageById(Long id);

    /**
     * Récupère tous les garages (paginé).
     * 
     * @param pageable les informations de pagination
     * @return la page de garages
     */
    Page<GarageResponse> getAllGarages(Pageable pageable);

    /**
     * Met à jour un garage existant.
     * 
     * @param id l'ID du garage
     * @param request les nouvelles données
     * @return le garage mis à jour
     */
    GarageResponse updateGarage(Long id, GarageRequest request);

    /**
     * Supprime un garage.
     * 
     * @param id l'ID du garage à supprimer
     */
    void deleteGarage(Long id);

    /**
     * Recherche des garages par nom.
     * 
     * @param name le nom du garage
     * @param pageable les informations de pagination
     * @return la page de garages correspondants
     */
    Page<GarageResponse> searchGaragesByName(String name, Pageable pageable);

    /**
     * Recherche des garages par ville.
     * 
     * @param city la ville
     * @param pageable les informations de pagination
     * @return la page de garages correspondants
     */
    Page<GarageResponse> searchGaragesByCity(String city, Pageable pageable);

    /**
     * Recherche des garages ayant des véhicules d'un type de carburant donné.
     * 
     * @param fuelType le type de carburant
     * @param pageable les informations de pagination
     * @return la page de garages correspondants
     */
    Page<GarageResponse> searchGaragesByFuelType(FuelType fuelType, Pageable pageable);

    /**
     * Recherche des garages ayant au moins un véhicule avec un accessoire d'un type donné.
     * 
     * @param accessoryType le type d'accessoire
     * @param pageable les informations de pagination
     * @return la page de garages correspondants
     */
    Page<GarageResponse> searchGaragesByAccessoryType(AccessoryType accessoryType, Pageable pageable);

    /**
     * Récupère les garages ayant de la capacité disponible.
     * 
     * @param pageable les informations de pagination
     * @return la page de garages avec capacité
     */
    Page<GarageResponse> getGaragesWithAvailableCapacity(Pageable pageable);
}
