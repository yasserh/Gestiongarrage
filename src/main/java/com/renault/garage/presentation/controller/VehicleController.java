package com.renault.garage.presentation.controller;

import com.renault.garage.application.dto.request.VehicleRequest;
import com.renault.garage.application.dto.response.VehicleResponse;
import com.renault.garage.domain.model.enums.FuelType;
import com.renault.garage.domain.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;

/**
 * Controller REST pour la gestion des véhicules.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "API de gestion des véhicules")
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Ajouter un véhicule à un garage")
    @PostMapping("/garage/{garageId}")
    public ResponseEntity<VehicleResponse> addVehicleToGarage(
            @PathVariable Long garageId,
            @Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.addVehicleToGarage(garageId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Récupérer un véhicule par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable Long id) {
        VehicleResponse response = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer tous les véhicules d'un garage")
    @GetMapping("/garage/{garageId}")
    public ResponseEntity<Page<VehicleResponse>> getVehiclesByGarage(
            @PathVariable Long garageId,
            @PageableDefault(size = 20, sort = "brand", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<VehicleResponse> response = vehicleService.getVehiclesByGarage(garageId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mettre à jour un véhicule")
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody VehicleRequest request) {
        VehicleResponse response = vehicleService.updateVehicle(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Supprimer un véhicule")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Rechercher des véhicules par modèle")
    @GetMapping("/search/by-model")
    public ResponseEntity<List<VehicleResponse>> findByModel(@RequestParam String model) {
        List<VehicleResponse> response = vehicleService.findVehiclesByModel(model);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Rechercher des véhicules par type de carburant")
    @GetMapping("/search/by-fuel-type")
    public ResponseEntity<Page<VehicleResponse>> findByFuelType(
            @RequestParam FuelType fuelType,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<VehicleResponse> response = vehicleService.findVehiclesByFuelType(fuelType, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer les véhicules écologiques")
    @GetMapping("/eco-friendly")
    public ResponseEntity<Page<VehicleResponse>> getEcoFriendlyVehicles(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<VehicleResponse> response = vehicleService.getEcoFriendlyVehicles(pageable);
        return ResponseEntity.ok(response);
    }
}
