package com.renault.garage.application.mapper;

import com.renault.garage.application.dto.request.AccessoryRequest;
import com.renault.garage.application.dto.response.AccessoryResponse;
import com.renault.garage.domain.model.Accessory;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper MapStruct pour l'entité Accessory.
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
public interface AccessoryMapper {

    /**
     * Convertit une entité Accessory en AccessoryResponse.
     * 
     * @param accessory l'entité à convertir
     * @return le DTO de réponse
     */
    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "vehicleDisplayName", expression = "java(accessory.getVehicle().getDisplayName())")
    AccessoryResponse toResponse(Accessory accessory);

    /**
     * Convertit une liste d'entités Accessory en liste de AccessoryResponse.
     * 
     * @param accessories la liste d'entités
     * @return la liste de DTOs
     */
    List<AccessoryResponse> toResponseList(List<Accessory> accessories);

    /**
     * Convertit un AccessoryRequest en entité Accessory.
     * 
     * @param request le DTO de requête
     * @return l'entité
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Accessory toEntity(AccessoryRequest request);

    /**
     * Met à jour une entité Accessory existante avec les données d'un AccessoryRequest.
     * 
     * @param request le DTO de requête
     * @param accessory l'entité à mettre à jour
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(AccessoryRequest request, @MappingTarget Accessory accessory);
}
