package com.renault.garage.domain.service;

import com.renault.garage.application.dto.request.VehicleRequest;
import com.renault.garage.application.dto.response.VehicleResponse;
import com.renault.garage.domain.model.enums.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface du service de gestion des véhicules.
 * 
 * Pattern utilisé: Service Layer Pattern
 * Principe SOLID: Dependency Inversion Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public interface VehicleService {

    /**
     * Ajoute un véhicule à un garage.
     * 
     * @param garageId l'ID du garage
     * @param request les données du véhicule
     * @return le véhicule créé
     */
    VehicleResponse addVehicleToGarage(Long garageId, VehicleRequest request);

    /**
     * Récupère un véhicule par son ID.
     * 
     * @param id l'ID du véhicule
     * @return le véhicule trouvé
     */
    VehicleResponse getVehicleById(Long id);

    /**
     * Récupère tous les véhicules d'un garage.
     * 
     * @param garageId l'ID du garage
     * @param pageable les informations de pagination
     * @return la page de véhicules
     */
    Page<VehicleResponse> getVehiclesByGarage(Long garageId, Pageable pageable);

    /**
     * Met à jour un véhicule existant.
     * 
     * @param id l'ID du véhicule
     * @param request les nouvelles données
     * @return le véhicule mis à jour
     */
    VehicleResponse updateVehicle(Long id, VehicleRequest request);

    /**
     * Supprime un véhicule.
     * 
     * @param id l'ID du véhicule à supprimer
     */
    void deleteVehicle(Long id);

    /**
     * Recherche tous les véhicules d'un modèle donné.
     * 
     * @param model le modèle recherché
     * @return la liste de véhicules
     */
    List<VehicleResponse> findVehiclesByModel(String model);

    /**
     * Recherche des véhicules par type de carburant.
     * 
     * @param fuelType le type de carburant
     * @param pageable les informations de pagination
     * @return la page de véhicules
     */
    Page<VehicleResponse> findVehiclesByFuelType(FuelType fuelType, Pageable pageable);

    /**
     * Récupère les véhicules écologiques.
     * 
     * @param pageable les informations de pagination
     * @return la page de véhicules écologiques
     */
    Page<VehicleResponse> getEcoFriendlyVehicles(Pageable pageable);
}
