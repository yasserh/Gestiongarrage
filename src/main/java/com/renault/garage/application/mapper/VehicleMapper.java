package com.renault.garage.application.mapper;

import com.renault.garage.application.dto.request.VehicleRequest;
import com.renault.garage.application.dto.response.VehicleResponse;
import com.renault.garage.domain.model.Vehicle;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct pour l'entité Vehicle.
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
public interface VehicleMapper {

    /**
     * Convertit une entité Vehicle en VehicleResponse.
     * 
     * @param vehicle l'entité à convertir
     * @return le DTO de réponse
     */
    @Mapping(target = "garageId", source = "garage.id")
    @Mapping(target = "garageName", source = "garage.name")
    @Mapping(target = "accessoryCount", expression = "java(vehicle.getAccessoryCount())")
    @Mapping(target = "isEcoFriendly", expression = "java(vehicle.isEcoFriendly())")
    @Mapping(target = "displayName", expression = "java(vehicle.getDisplayName())")
    VehicleResponse toResponse(Vehicle vehicle);

    /**
     * Convertit une liste d'entités Vehicle en liste de VehicleResponse.
     * 
     * @param vehicles la liste d'entités
     * @return la liste de DTOs
     */
    List<VehicleResponse> toResponseList(List<Vehicle> vehicles);

    /**
     * Convertit un VehicleRequest en entité Vehicle.
     * 
     * @param request le DTO de requête
     * @return l'entité
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Vehicle toEntity(VehicleRequest request);

    /**
     * Met à jour une entité Vehicle existante avec les données d'un VehicleRequest.
     * 
     * @param request le DTO de requête
     * @param vehicle l'entité à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(VehicleRequest request, @MappingTarget Vehicle vehicle);
}
