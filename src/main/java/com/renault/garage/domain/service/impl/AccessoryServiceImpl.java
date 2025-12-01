package com.renault.garage.domain.service.impl;

import com.renault.garage.application.dto.request.AccessoryRequest;
import com.renault.garage.application.dto.response.AccessoryResponse;
import com.renault.garage.application.mapper.AccessoryMapper;
import com.renault.garage.domain.exception.AccessoryNotFoundException;
import com.renault.garage.domain.exception.VehicleNotFoundException;
import com.renault.garage.domain.model.Accessory;
import com.renault.garage.domain.model.Vehicle;
import com.renault.garage.domain.model.enums.AccessoryType;
import com.renault.garage.domain.repository.AccessoryRepository;
import com.renault.garage.domain.repository.VehicleRepository;
import com.renault.garage.domain.service.AccessoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implémentation du service de gestion des accessoires.
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
public class AccessoryServiceImpl implements AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper accessoryMapper;

    @Override
    public AccessoryResponse addAccessoryToVehicle(Long vehicleId, AccessoryRequest request) {
        log.info("Adding accessory to vehicle ID: {}", vehicleId);
        
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
            .orElseThrow(() -> new VehicleNotFoundException(vehicleId));
        
        Accessory accessory = accessoryMapper.toEntity(request);
        vehicle.addAccessory(accessory);
        
        Accessory savedAccessory = accessoryRepository.save(accessory);
        
        log.info("Accessory added successfully with ID: {}", savedAccessory.getId());
        return accessoryMapper.toResponse(savedAccessory);
    }

    @Override
    @Transactional(readOnly = true)
    public AccessoryResponse getAccessoryById(Long id) {
        log.debug("Fetching accessory with ID: {}", id);
        
        Accessory accessory = accessoryRepository.findById(id)
            .orElseThrow(() -> new AccessoryNotFoundException(id));
        
        return accessoryMapper.toResponse(accessory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccessoryResponse> getAccessoriesByVehicle(Long vehicleId) {
        log.debug("Fetching accessories for vehicle ID: {}", vehicleId);
        
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new VehicleNotFoundException(vehicleId);
        }
        
        List<Accessory> accessories = accessoryRepository.findByVehicleId(vehicleId);
        return accessoryMapper.toResponseList(accessories);
    }

    @Override
    public AccessoryResponse updateAccessory(Long id, AccessoryRequest request) {
        log.info("Updating accessory with ID: {}", id);
        
        Accessory accessory = accessoryRepository.findById(id)
            .orElseThrow(() -> new AccessoryNotFoundException(id));
        
        accessoryMapper.updateEntityFromRequest(request, accessory);
        Accessory updatedAccessory = accessoryRepository.save(accessory);
        
        log.info("Accessory updated successfully: {}", id);
        return accessoryMapper.toResponse(updatedAccessory);
    }

    @Override
    public void deleteAccessory(Long id) {
        log.info("Deleting accessory with ID: {}", id);
        
        Accessory accessory = accessoryRepository.findById(id)
            .orElseThrow(() -> new AccessoryNotFoundException(id));
        
        accessory.getVehicle().removeAccessory(accessory);
        
        accessoryRepository.delete(accessory);
        log.info("Accessory deleted successfully: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccessoryResponse> findAccessoriesByType(AccessoryType type, Pageable pageable) {
        log.debug("Searching accessories by type: {}", type);
        
        Page<Accessory> accessories = accessoryRepository.findByType(type, pageable);
        return accessories.map(accessoryMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPriceByVehicle(Long vehicleId) {
        log.debug("Calculating total accessory price for vehicle ID: {}", vehicleId);
        
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new VehicleNotFoundException(vehicleId);
        }
        
        return accessoryRepository.calculateTotalPriceByVehicle(vehicleId);
    }
}
