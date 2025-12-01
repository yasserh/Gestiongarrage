package com.renault.garage.presentation.controller;

import com.renault.garage.application.dto.request.AccessoryRequest;
import com.renault.garage.application.dto.response.AccessoryResponse;
import com.renault.garage.domain.model.enums.AccessoryType;
import com.renault.garage.domain.service.AccessoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller REST pour la gestion des accessoires.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/accessories")
@RequiredArgsConstructor
@Tag(name = "Accessoires", description = "API de gestion des accessoires")
public class AccessoryController {

    private final AccessoryService accessoryService;

    @Operation(summary = "Ajouter un accessoire à un véhicule")
    @PostMapping("/vehicle/{vehicleId}")
    public ResponseEntity<AccessoryResponse> addAccessoryToVehicle(
            @PathVariable Long vehicleId,
            @Valid @RequestBody AccessoryRequest request) {
        AccessoryResponse response = accessoryService.addAccessoryToVehicle(vehicleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Récupérer un accessoire par son ID")
    @GetMapping("/{id}")
    public ResponseEntity<AccessoryResponse> getAccessoryById(@PathVariable Long id) {
        AccessoryResponse response = accessoryService.getAccessoryById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Récupérer tous les accessoires d'un véhicule")
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<AccessoryResponse>> getAccessoriesByVehicle(@PathVariable Long vehicleId) {
        List<AccessoryResponse> response = accessoryService.getAccessoriesByVehicle(vehicleId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Mettre à jour un accessoire")
    @PutMapping("/{id}")
    public ResponseEntity<AccessoryResponse> updateAccessory(
            @PathVariable Long id,
            @Valid @RequestBody AccessoryRequest request) {
        AccessoryResponse response = accessoryService.updateAccessory(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Supprimer un accessoire")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessory(@PathVariable Long id) {
        accessoryService.deleteAccessory(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Rechercher des accessoires par type")
    @GetMapping("/search/by-type")
    public ResponseEntity<Page<AccessoryResponse>> findByType(
            @RequestParam AccessoryType type,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<AccessoryResponse> response = accessoryService.findAccessoriesByType(type, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Calculer le prix total des accessoires d'un véhicule")
    @GetMapping("/vehicle/{vehicleId}/total-price")
    public ResponseEntity<BigDecimal> calculateTotalPrice(@PathVariable Long vehicleId) {
        BigDecimal totalPrice = accessoryService.calculateTotalPriceByVehicle(vehicleId);
        return ResponseEntity.ok(totalPrice);
    }
}
