package com.renault.garage.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.application.dto.request.GarageRequest;
import com.renault.garage.application.dto.response.GarageResponse;
import com.renault.garage.config.SecurityConfig;
import com.renault.garage.domain.service.GarageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration pour GarageController.
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@WebMvcTest(GarageController.class)
@Import(SecurityConfig.class) // 1. Charge votre config (qui désactive CSRF)
@MockBean(JpaMetamodelMappingContext.class) // (Si nécessaire pour l'erreur JPA précédente)
@DisplayName("Tests d'intégration - GarageController")
class GarageControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GarageService garageService;

    private GarageRequest garageRequest;
    private GarageResponse garageResponse;

    @BeforeEach
    void setUp() {
        // Initialisation des horaires
        Map<DayOfWeek, String> openingHours = Map.of(
                DayOfWeek.MONDAY, "08:00-19:00",
                DayOfWeek.TUESDAY, "08:00-19:00"
        );
        garageRequest = GarageRequest.builder()
            .name("Garage Renault Paris")
            .address("123 Rue de la République, 75001 Paris")
            .telephone("+33123456789")
            .email("paris@renault.com")
                .openingHours(openingHours) // <--- LIGNE AJOUTÉE ICI
            .build();

        garageResponse = GarageResponse.builder()
            .id(1L)
            .name("Garage Renault Paris")
            .address("123 Rue de la République, 75001 Paris")
            .telephone("+33123456789")
            .email("paris@renault.com")
                .openingHours(openingHours) // Pensez à l'ajouter aussi à la réponse mockée pour la cohérence
            .vehicleCount(0)
            .availableCapacity(50)
            .isFull(false)
            .build();
    }

    @Test
    @DisplayName("POST /garages - Devrait créer un garage")
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // 2. Simule un utilisateur connecté
    void shouldCreateGarage() throws Exception {
        // Given
        when(garageService.createGarage(any(GarageRequest.class))).thenReturn(garageResponse);

        // When & Then
        mockMvc.perform(post("/garages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(garageRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Garage Renault Paris"))
            .andExpect(jsonPath("$.email").value("paris@renault.com"));
    }

    @Test
    @DisplayName("GET /garages/{id} - Devrait récupérer un garage")
    @WithMockUser // Simule un utilisateur par défaut
    void shouldGetGarageById() throws Exception {
        // Given
        when(garageService.getGarageById(1L)).thenReturn(garageResponse);

        // When & Then
        mockMvc.perform(get("/garages/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Garage Renault Paris"));
    }

    @Test
    @DisplayName("GET /garages - Devrait récupérer tous les garages")
    @WithMockUser // Simule un utilisateur par défaut
    void shouldGetAllGarages() throws Exception {
        // Given
        Page<GarageResponse> page = new PageImpl<>(List.of(garageResponse), PageRequest.of(0, 20), 1);
        when(garageService.getAllGarages(any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/garages"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content[0].name").value("Garage Renault Paris"));
    }

    @Test
    @DisplayName("DELETE /garages/{id} - Devrait supprimer un garage")
    @WithMockUser // Simule un utilisateur par défaut
    void shouldDeleteGarage() throws Exception {
        // When & Then
        mockMvc.perform(delete("/garages/1"))
            .andExpect(status().isNoContent());
    }
}
