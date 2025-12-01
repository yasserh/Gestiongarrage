package com.renault.garage.domain.model;

import com.renault.garage.domain.exception.VehicleQuotaExceededException;
import com.renault.garage.domain.model.enums.FuelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests unitaires pour la contrainte métier du quota de véhicules.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@DisplayName("Tests - Contrainte Quota Véhicules")
class GarageVehicleQuotaTest {

    private Garage garage;

    @BeforeEach
    void setUp() {
        garage = Garage.builder()
            .id(1L)
            .name("Test Garage")
            .address("Test Address")
            .telephone("+33123456789")
            .email("test@renault.com")
            .build();
    }

    @Test
    @DisplayName("Devrait permettre d'ajouter un véhicule quand le garage n'est pas plein")
    void shouldAddVehicleWhenGarageNotFull() {
        // Given
        Vehicle vehicle = Vehicle.builder()
            .brand("Renault")
            .model("Clio")
            .yearOfManufacture(2023)
            .fuelType(FuelType.ESSENCE)
            .build();

        // When
        garage.addVehicle(vehicle);

        // Then
        assertThat(garage.getVehicles()).hasSize(1);
        assertThat(garage.canAcceptVehicle()).isTrue();
        assertThat(vehicle.getGarage()).isEqualTo(garage);
    }

    @Test
    @DisplayName("Devrait lever VehicleQuotaExceededException quand le quota est atteint")
    void shouldThrowExceptionWhenQuotaExceeded() {
        // Given - Ajouter 50 véhicules (quota max)
        for (int i = 0; i < Garage.MAX_VEHICLES_PER_GARAGE; i++) {
            Vehicle vehicle = Vehicle.builder()
                .brand("Renault")
                .model("Clio " + i)
                .yearOfManufacture(2023)
                .fuelType(FuelType.ESSENCE)
                .build();
            garage.addVehicle(vehicle);
        }

        assertThat(garage.getVehicles()).hasSize(50);
        assertThat(garage.canAcceptVehicle()).isFalse();

        // When & Then - Tenter d'ajouter un 51ème véhicule
        Vehicle extraVehicle = Vehicle.builder()
            .brand("Renault")
            .model("Megane")
            .yearOfManufacture(2023)
            .fuelType(FuelType.DIESEL)
            .build();

        assertThatThrownBy(() -> garage.addVehicle(extraVehicle))
            .isInstanceOf(VehicleQuotaExceededException.class)
            .hasMessageContaining("quota maximum de 50 véhicules");
    }

    @Test
    @DisplayName("Devrait retirer un véhicule du garage")
    void shouldRemoveVehicleFromGarage() {
        // Given
        Vehicle vehicle = Vehicle.builder()
            .brand("Renault")
            .model("Clio")
            .yearOfManufacture(2023)
            .fuelType(FuelType.ESSENCE)
            .build();
        garage.addVehicle(vehicle);

        // When
        garage.removeVehicle(vehicle);

        // Then
        assertThat(garage.getVehicles()).isEmpty();
        assertThat(vehicle.getGarage()).isNull();
    }

    @Test
    @DisplayName("Devrait calculer correctement le nombre de véhicules")
    void shouldCalculateVehicleCountCorrectly() {
        // Given & When
        for (int i = 0; i < 10; i++) {
            Vehicle vehicle = Vehicle.builder()
                .brand("Renault")
                .model("Clio " + i)
                .yearOfManufacture(2023)
                .fuelType(FuelType.ESSENCE)
                .build();
            garage.addVehicle(vehicle);
        }

        // Then
        assertThat(garage.getVehicleCount()).isEqualTo(10);
        assertThat(garage.canAcceptVehicle()).isTrue();
    }
}
