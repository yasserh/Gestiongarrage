package com.renault.garage.domain.service.impl;

import com.renault.garage.application.dto.request.GarageRequest;
import com.renault.garage.application.dto.response.GarageResponse;
import com.renault.garage.application.mapper.GarageMapper;
import com.renault.garage.domain.exception.GarageNotFoundException;
import com.renault.garage.domain.model.Garage;
import com.renault.garage.domain.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour GarageServiceImpl.
 * 
 * Approche TDD : Test-Driven Development
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - GarageService")
class GarageServiceImplTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper garageMapper;

    @InjectMocks
    private GarageServiceImpl garageService;

    private GarageRequest garageRequest;
    private Garage garage;
    private GarageResponse garageResponse;

    @BeforeEach
    void setUp() {
        garageRequest = GarageRequest.builder()
            .name("Garage Renault Paris")
            .address("123 Rue de la République, 75001 Paris")
            .telephone("+33123456789")
            .email("paris@renault.com")
            .build();

        garage = Garage.builder()
            .id(1L)
            .name("Garage Renault Paris")
            .address("123 Rue de la République, 75001 Paris")
            .telephone("+33123456789")
            .email("paris@renault.com")
            .build();

        garageResponse = GarageResponse.builder()
            .id(1L)
            .name("Garage Renault Paris")
            .address("123 Rue de la République, 75001 Paris")
            .telephone("+33123456789")
            .email("paris@renault.com")
            .vehicleCount(0)
            .availableCapacity(50)
            .isFull(false)
            .build();
    }

    @Test
    @DisplayName("Devrait créer un garage avec succès")
    void shouldCreateGarageSuccessfully() {
        // Given
        when(garageRepository.existsByEmail(garageRequest.getEmail())).thenReturn(false);
        when(garageMapper.toEntity(garageRequest)).thenReturn(garage);
        when(garageRepository.save(garage)).thenReturn(garage);
        when(garageMapper.toResponse(garage)).thenReturn(garageResponse);

        // When
        GarageResponse result = garageService.createGarage(garageRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Garage Renault Paris");
        assertThat(result.getEmail()).isEqualTo("paris@renault.com");
        
        verify(garageRepository).existsByEmail(garageRequest.getEmail());
        verify(garageRepository).save(garage);
    }

    @Test
    @DisplayName("Devrait lever une exception si l'email existe déjà")
    void shouldThrowExceptionWhenEmailExists() {
        // Given
        when(garageRepository.existsByEmail(garageRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> garageService.createGarage(garageRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Un garage avec cet email existe déjà");
        
        verify(garageRepository, never()).save(any());
    }

    @Test
    @DisplayName("Devrait récupérer un garage par son ID")
    void shouldGetGarageById() {
        // Given
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageMapper.toResponse(garage)).thenReturn(garageResponse);

        // When
        GarageResponse result = garageService.getGarageById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        
        verify(garageRepository).findById(1L);
    }

    @Test
    @DisplayName("Devrait lever GarageNotFoundException si le garage n'existe pas")
    void shouldThrowGarageNotFoundExceptionWhenGarageDoesNotExist() {
        // Given
        when(garageRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> garageService.getGarageById(999L))
            .isInstanceOf(GarageNotFoundException.class);
        
        verify(garageRepository).findById(999L);
    }

    @Test
    @DisplayName("Devrait supprimer un garage")
    void shouldDeleteGarage() {
        // Given
        when(garageRepository.existsById(1L)).thenReturn(true);

        // When
        garageService.deleteGarage(1L);

        // Then
        verify(garageRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Devrait lever une exception lors de la suppression d'un garage inexistant")
    void shouldThrowExceptionWhenDeletingNonExistentGarage() {
        // Given
        when(garageRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> garageService.deleteGarage(999L))
            .isInstanceOf(GarageNotFoundException.class);
        
        verify(garageRepository, never()).deleteById(any());
    }
}
