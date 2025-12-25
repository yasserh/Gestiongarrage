package com.renault.garage.domain.service.impl;

import com.renault.garage.application.dto.request.GarageRequest;
import com.renault.garage.application.dto.response.GarageResponse;
import com.renault.garage.application.dto.response.VehicleResponse;
import com.renault.garage.application.mapper.GarageMapper;
import com.renault.garage.application.mapper.VehicleMapper;
import com.renault.garage.domain.exception.GarageNotFoundException;
import com.renault.garage.domain.model.Garage;
import com.renault.garage.domain.model.enums.AccessoryType;
import com.renault.garage.domain.model.enums.FuelType;
import com.renault.garage.domain.repository.GarageRepository;
import com.renault.garage.domain.repository.specification.GarageSpecifications;
import com.renault.garage.domain.service.GarageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des garages.
 * 
 * Pattern utilisé: Service Layer Pattern
 * Principe SOLID: Single Responsibility Principle, Dependency Inversion Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;
    private final VehicleMapper vehicleMapper; // Injection du VehicleMapper
    @Override
    public GarageResponse createGarage(GarageRequest request) {
        log.info("Creating new garage: {}", request.getName());
        
        // Vérifier si l'email existe déjà
        if (garageRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un garage avec cet email existe déjà");
        }
        
        Garage garage = garageMapper.toEntity(request);
        Garage savedGarage = garageRepository.save(garage);
        
        log.info("Garage created successfully with ID: {}", savedGarage.getId());
        return garageMapper.toResponse(savedGarage);
    }

    @Override
    @Transactional(readOnly = true)
    public GarageResponse getGarageById(Long id) {
        log.debug("Fetching garage with ID: {}", id);
        
        Garage garage = garageRepository.findById(id)
            .orElseThrow(() -> new GarageNotFoundException(id));
        
        return garageMapper.toResponse(garage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageResponse> getAllGarages(Pageable pageable) {
        log.debug("Fetching all garages with pagination: {}", pageable);
        
        Page<Garage> garages = garageRepository.findAll(pageable);
        return garages.map(garageMapper::toResponse);
    }

    @Override
    public GarageResponse updateGarage(Long id, GarageRequest request) {
        log.info("Updating garage with ID: {}", id);
        
        Garage garage = garageRepository.findById(id)
            .orElseThrow(() -> new GarageNotFoundException(id));
        
        // Vérifier si le nouvel email existe déjà (sauf pour ce garage)
        if (!garage.getEmail().equals(request.getEmail()) && 
            garageRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Un garage avec cet email existe déjà");
        }
        
        garageMapper.updateEntityFromRequest(request, garage);
        Garage updatedGarage = garageRepository.save(garage);
        
        log.info("Garage updated successfully: {}", id);
        return garageMapper.toResponse(updatedGarage);
    }

    @Override
    public void deleteGarage(Long id) {
        log.info("Deleting garage with ID: {}", id);
        
        if (!garageRepository.existsById(id)) {
            throw new GarageNotFoundException(id);
        }
        
        garageRepository.deleteById(id);
        log.info("Garage deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageResponse> searchGaragesByName(String name, Pageable pageable) {
        log.debug("Searching garages by name: {}", name);
        
        Specification<Garage> spec = GarageSpecifications.hasName(name);
        Page<Garage> garages = garageRepository.findAll(spec, pageable);
        
        return garages.map(garageMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageResponse> searchGaragesByCity(String city, Pageable pageable) {
        log.debug("Searching garages by city: {}", city);
        
        Specification<Garage> spec = GarageSpecifications.hasCity(city);
        Page<Garage> garages = garageRepository.findAll(spec, pageable);
        
        return garages.map(garageMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageResponse> searchGaragesByFuelType(FuelType fuelType, Pageable pageable) {
        log.debug("Searching garages by fuel type: {}", fuelType);
        
        Specification<Garage> spec = GarageSpecifications.hasVehicleWithFuelType(fuelType);
        Page<Garage> garages = garageRepository.findAll(spec, pageable);
        
        return garages.map(garageMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageResponse> searchGaragesByAccessoryType(AccessoryType accessoryType, Pageable pageable) {
        log.debug("Searching garages by accessory type: {}", accessoryType);

        // 1. Récupérer les garages qui correspondent au critère (via la spécification existante)
        Specification<Garage> spec = GarageSpecifications.hasVehicleWithAccessoryType(accessoryType);
        Page<Garage> garages = garageRepository.findAll(spec, pageable);

        // 2. Mapper en réponse et peupler les véhicules filtrés
        return garages.map(garage -> {
            GarageResponse response = garageMapper.toResponse(garage);

            // Filtrer la liste des véhicules du garage pour ne garder que ceux ayant l'accessoire
            List<VehicleResponse> matchingVehicles = garage.getVehicles().stream()
                    .filter(vehicle -> vehicle.getAccessories().stream()
                            .anyMatch(accessory -> accessory.getType() == accessoryType))
                    .map(vehicleMapper::toResponse)
                    .collect(Collectors.toList());

            // Ajouter la liste filtrée à la réponse
            response.setVehicles(matchingVehicles);

            return response;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GarageResponse> getGaragesWithAvailableCapacity(Pageable pageable) {
        log.debug("Fetching garages with available capacity");
        
        Page<Garage> garages = garageRepository.findGaragesWithAvailableCapacity(
            Garage.MAX_VEHICLES_PER_GARAGE, 
            pageable
        );
        
        return garages.map(garageMapper::toResponse);
    }
}
