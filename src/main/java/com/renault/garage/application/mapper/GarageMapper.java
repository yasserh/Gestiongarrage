package com.renault.garage.application.mapper;

import com.renault.garage.application.dto.request.GarageRequest;
import com.renault.garage.application.dto.response.GarageResponse;
import com.renault.garage.domain.model.Garage;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct pour l'entité Garage.
 * 
 * Convertit entre les entités et les DTOs de manière automatique.
 * 
 * Pattern utilisé: DTO Pattern, Mapper Pattern
 * Principe SOLID: Single Responsibility Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface GarageMapper {

    /**
     * Convertit une entité Garage en GarageResponse.
     * 
     * @param garage l'entité à convertir
     * @return le DTO de réponse
     */
    @Mapping(target = "vehicleCount", expression = "java(garage.getVehicleCount())")
    @Mapping(target = "availableCapacity", expression = "java(com.renault.garage.domain.model.Garage.MAX_VEHICLES_PER_GARAGE - garage.getVehicleCount())")
    @Mapping(target = "isFull", expression = "java(!garage.canAcceptVehicle())")
    GarageResponse toResponse(Garage garage);

    /**
     * Convertit une liste d'entités Garage en liste de GarageResponse.
     * 
     * @param garages la liste d'entités
     * @return la liste de DTOs
     */
    List<GarageResponse> toResponseList(List<Garage> garages);

    /**
     * Convertit un GarageRequest en entité Garage.
     * 
     * @param request le DTO de requête
     * @return l'entité
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Garage toEntity(GarageRequest request);

    /**
     * Met à jour une entité Garage existante avec les données d'un GarageRequest.
     * 
     * @param request le DTO de requête
     * @param garage l'entité à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(GarageRequest request, @MappingTarget Garage garage);
}
