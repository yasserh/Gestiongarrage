package com.renault.garage.domain.service.impl;

import com.renault.garage.application.dto.request.VehicleRequest;
import com.renault.garage.application.dto.response.VehicleResponse;
import com.renault.garage.application.mapper.VehicleMapper;
import com.renault.garage.domain.exception.GarageNotFoundException;
import com.renault.garage.domain.exception.VehicleNotFoundException;
import com.renault.garage.domain.model.Garage;
import com.renault.garage.domain.model.Vehicle;
import com.renault.garage.domain.model.enums.FuelType;
import com.renault.garage.domain.repository.GarageRepository;
import com.renault.garage.domain.repository.VehicleRepository;
import com.renault.garage.domain.service.VehicleService;
import com.renault.garage.infrastructure.kafka.event.VehicleCreatedEvent;
import com.renault.garage.infrastructure.kafka.producer.VehicleEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service de gestion des véhicules.
 * 
 * Pattern utilisé: Service Layer Pattern
 * Principe SOLID: Single Responsibility Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final VehicleMapper vehicleMapper;
    private final VehicleEventProducer vehicleEventProducer;

    @Override
    public VehicleResponse addVehicleToGarage(Long garageId, VehicleRequest request) {
        log.info("Adding vehicle to garage ID: {}", garageId);
        
        Garage garage = garageRepository.findById(garageId)
            .orElseThrow(() -> new GarageNotFoundException(garageId));
        
        // Vérifier si le VIN existe déjà
        if (request.getVin() != null && vehicleRepository.existsByVin(request.getVin())) {
            throw new IllegalArgumentException("Un véhicule avec ce VIN existe déjà");
        }
        
        Vehicle vehicle = vehicleMapper.toEntity(request);
        
        // Ajouter le véhicule au garage (vérifie automatiquement le quota)
        garage.addVehicle(vehicle);
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        
        // Publier l'événement Kafka
        publishVehicleCreatedEvent(savedVehicle);
        
        log.info("Vehicle added successfully with ID: {}", savedVehicle.getId());
        return vehicleMapper.toResponse(savedVehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getVehicleById(Long id) {
        log.debug("Fetching vehicle with ID: {}", id);
        
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new VehicleNotFoundException(id));
        
        return vehicleMapper.toResponse(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponse> getVehiclesByGarage(Long garageId, Pageable pageable) {
        log.debug("Fetching vehicles for garage ID: {}", garageId);
        
        if (!garageRepository.existsById(garageId)) {
            throw new GarageNotFoundException(garageId);
        }
        
        Page<Vehicle> vehicles = vehicleRepository.findByGarageId(garageId, pageable);
        return vehicles.map(vehicleMapper::toResponse);
    }

    @Override
    public VehicleResponse updateVehicle(Long id, VehicleRequest request) {
        log.info("Updating vehicle with ID: {}", id);
        
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new VehicleNotFoundException(id));
        
        // Vérifier si le nouveau VIN existe déjà (sauf pour ce véhicule)
        if (request.getVin() != null && 
            !request.getVin().equals(vehicle.getVin()) && 
            vehicleRepository.existsByVin(request.getVin())) {
            throw new IllegalArgumentException("Un véhicule avec ce VIN existe déjà");
        }
        
        vehicleMapper.updateEntityFromRequest(request, vehicle);
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        
        log.info("Vehicle updated successfully: {}", id);
        return vehicleMapper.toResponse(updatedVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        log.info("Deleting vehicle with ID: {}", id);
        
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new VehicleNotFoundException(id));
        
        // Retirer le véhicule du garage
        vehicle.getGarage().removeVehicle(vehicle);
        
        vehicleRepository.delete(vehicle);
        log.info("Vehicle deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> findVehiclesByModel(String model) {
        log.debug("Searching vehicles by model: {}", model);
        
        List<Vehicle> vehicles = vehicleRepository.findAllByModel(model);
        return vehicleMapper.toResponseList(vehicles);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponse> findVehiclesByFuelType(FuelType fuelType, Pageable pageable) {
        log.debug("Searching vehicles by fuel type: {}", fuelType);
        
        Page<Vehicle> vehicles = vehicleRepository.findByFuelType(fuelType, pageable);
        return vehicles.map(vehicleMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VehicleResponse> getEcoFriendlyVehicles(Pageable pageable) {
        log.debug("Fetching eco-friendly vehicles");
        
        Page<Vehicle> vehicles = vehicleRepository.findEcoFriendlyVehicles(pageable);
        return vehicles.map(vehicleMapper::toResponse);
    }

    /**
     * Publie un événement Kafka lors de la création d'un véhicule.
     * 
     * @param vehicle le véhicule créé
     */
    private void publishVehicleCreatedEvent(Vehicle vehicle) {
        VehicleCreatedEvent event = VehicleCreatedEvent.builder()
            .vehicleId(vehicle.getId())
            .brand(vehicle.getBrand())
            .model(vehicle.getModel())
            .yearOfManufacture(vehicle.getYearOfManufacture())
            .fuelType(vehicle.getFuelType())
            .vin(vehicle.getVin())
            .garageId(vehicle.getGarage().getId())
            .garageName(vehicle.getGarage().getName())
            .createdAt(LocalDateTime.now())
            .eventId(UUID.randomUUID().toString())
            .build();
        
        vehicleEventProducer.publishVehicleCreatedEvent(event);
    }
}
