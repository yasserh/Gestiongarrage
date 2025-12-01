package com.renault.garage.domain.service;

import com.renault.garage.application.dto.request.AccessoryRequest;
import com.renault.garage.application.dto.response.AccessoryResponse;
import com.renault.garage.domain.model.enums.AccessoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface du service de gestion des accessoires.
 * 
 * Pattern utilisé: Service Layer Pattern
 * Principe SOLID: Dependency Inversion Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
public interface AccessoryService {

    /**
     * Ajoute un accessoire à un véhicule.
     * 
     * @param vehicleId l'ID du véhicule
     * @param request les données de l'accessoire
     * @return l'accessoire créé
     */
    AccessoryResponse addAccessoryToVehicle(Long vehicleId, AccessoryRequest request);

    /**
     * Récupère un accessoire par son ID.
     * 
     * @param id l'ID de l'accessoire
     * @return l'accessoire trouvé
     */
    AccessoryResponse getAccessoryById(Long id);

    /**
     * Récupère tous les accessoires d'un véhicule.
     * 
     * @param vehicleId l'ID du véhicule
     * @return la liste d'accessoires
     */
    List<AccessoryResponse> getAccessoriesByVehicle(Long vehicleId);

    /**
     * Met à jour un accessoire existant.
     * 
     * @param id l'ID de l'accessoire
     * @param request les nouvelles données
     * @return l'accessoire mis à jour
     */
    AccessoryResponse updateAccessory(Long id, AccessoryRequest request);

    /**
     * Supprime un accessoire.
     * 
     * @param id l'ID de l'accessoire à supprimer
     */
    void deleteAccessory(Long id);

    /**
     * Recherche des accessoires par type.
     * 
     * @param type le type d'accessoire
     * @param pageable les informations de pagination
     * @return la page d'accessoires
     */
    Page<AccessoryResponse> findAccessoriesByType(AccessoryType type, Pageable pageable);

    /**
     * Calcule le prix total des accessoires d'un véhicule.
     * 
     * @param vehicleId l'ID du véhicule
     * @return le prix total
     */
    BigDecimal calculateTotalPriceByVehicle(Long vehicleId);
}
