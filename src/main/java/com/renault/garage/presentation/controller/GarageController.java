package com.renault.garage.presentation.controller;

import com.renault.garage.application.dto.request.GarageRequest;
import com.renault.garage.application.dto.response.GarageResponse;
import com.renault.garage.domain.model.enums.AccessoryType;
import com.renault.garage.domain.model.enums.FuelType;
import com.renault.garage.domain.service.GarageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST pour la gestion des garages.
 * 
 * Expose les endpoints CRUD et de recherche pour les garages.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/garages")
@RequiredArgsConstructor
@Tag(name = "Garages", description = "API de gestion des garages Renault")
public class GarageController {

    private final GarageService garageService;

    @Operation(summary = "Créer un nouveau garage")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Garage créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<GarageResponse> createGarage(@Valid @RequestBody GarageRequest request) {
        GarageResponse response = garageService.createGarage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Récupérer un garage par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Garage trouvé"),
        @ApiResponse(responseCode = "404", description = "Garage non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GarageResponse> getGarageById(
            @Parameter(description = "ID du garage") @PathVariable Long id) {
        GarageResponse response = garageService.getGarageById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer tous les garages (paginé)")
    @GetMapping
    public ResponseEntity<Page<GarageResponse>> getAllGarages(
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<GarageResponse> response = garageService.getAllGarages(pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mettre à jour un garage")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Garage mis à jour"),
        @ApiResponse(responseCode = "404", description = "Garage non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<GarageResponse> updateGarage(
            @PathVariable Long id,
            @Valid @RequestBody GarageRequest request) {
        GarageResponse response = garageService.updateGarage(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Supprimer un garage")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Garage supprimé"),
        @ApiResponse(responseCode = "404", description = "Garage non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarage(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Rechercher des garages par nom")
    @GetMapping("/search/by-name")
    public ResponseEntity<Page<GarageResponse>> searchByName(
            @RequestParam String name,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<GarageResponse> response = garageService.searchGaragesByName(name, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Rechercher des garages par ville")
    @GetMapping("/search/by-city")
    public ResponseEntity<Page<GarageResponse>> searchByCity(
            @RequestParam String city,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<GarageResponse> response = garageService.searchGaragesByCity(city, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Rechercher des garages par type de carburant")
    @GetMapping("/search/by-fuel-type")
    public ResponseEntity<Page<GarageResponse>> searchByFuelType(
            @RequestParam FuelType fuelType,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<GarageResponse> response = garageService.searchGaragesByFuelType(fuelType, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Rechercher des garages par type d'accessoire")
    @GetMapping("/search/by-accessory-type")
    public ResponseEntity<Page<GarageResponse>> searchByAccessoryType(
            @RequestParam AccessoryType accessoryType,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<GarageResponse> response = garageService.searchGaragesByAccessoryType(accessoryType, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer les garages avec capacité disponible")
    @GetMapping("/available-capacity")
    public ResponseEntity<Page<GarageResponse>> getGaragesWithAvailableCapacity(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<GarageResponse> response = garageService.getGaragesWithAvailableCapacity(pageable);
        return ResponseEntity.ok(response);
    }
}
