package com.renault.garage.domain.model;

import com.renault.garage.domain.exception.VehicleQuotaExceededException;
import com.renault.garage.domain.model.valueobject.OpeningTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entité représentant un garage Renault.
 * 
 * Un garage possède des informations de contact, des horaires d'ouverture
 * et peut stocker jusqu'à 50 véhicules (contrainte métier).
 * 
 * Pattern utilisé: Entity (DDD), Aggregate Root
 * Principe SOLID: Single Responsibility Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Entity
@Table(name = "garages", indexes = {
    @Index(name = "idx_garage_name", columnList = "name"),
    @Index(name = "idx_garage_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "vehicles")
@EqualsAndHashCode(of = "id")
public class Garage {

    /**
     * Quota maximum de véhicules par garage (contrainte métier)
     */
    public static final int MAX_VEHICLES_PER_GARAGE = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom du garage (obligatoire)
     */
    @NotBlank(message = "Le nom du garage est obligatoire")
    @Size(min = 3, max = 100, message = "Le nom doit contenir entre 3 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Adresse du garage (obligatoire)
     */
    @NotBlank(message = "L'adresse du garage est obligatoire")
    @Size(min = 10, max = 255, message = "L'adresse doit contenir entre 10 et 255 caractères")
    @Column(nullable = false, length = 255)
    private String address;

    /**
     * Numéro de téléphone (obligatoire)
     */
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Le numéro de téléphone doit contenir entre 10 et 15 chiffres")
    @Column(nullable = false, length = 20)
    private String telephone;

    /**
     * Email du garage (obligatoire)
     */
    @NotBlank(message = "L'email du garage est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Horaires d'ouverture par jour de la semaine
     * Stockés sous forme de texte simple (ex: "08:00-12:00,14:00-18:00")
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "garage_opening_hours",
        joinColumns = @JoinColumn(name = "garage_id")
    )
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "day_of_week")
    @Column(name = "hours", length = 500)
    @Builder.Default
    private Map<DayOfWeek, String> openingHours = new HashMap<>();

    /**
     * Liste des véhicules stockés dans ce garage
     */
    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();

    /**
     * Date de création de l'enregistrement
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date de dernière modification de l'enregistrement
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Ajoute un véhicule au garage en vérifiant le quota.
     * 
     * @param vehicle le véhicule à ajouter
     * @throws VehicleQuotaExceededException si le quota est dépassé
     */
    public void addVehicle(Vehicle vehicle) {
        if (vehicles.size() >= MAX_VEHICLES_PER_GARAGE) {
            throw new VehicleQuotaExceededException(this.id);
        }
        vehicles.add(vehicle);
        vehicle.setGarage(this);
    }

    /**
     * Retire un véhicule du garage.
     * 
     * @param vehicle le véhicule à retirer
     */
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setGarage(null);
    }

    /**
     * Vérifie si le garage peut accepter un nouveau véhicule.
     * 
     * @return true si le quota n'est pas atteint
     */
    public boolean canAcceptVehicle() {
        return vehicles.size() < MAX_VEHICLES_PER_GARAGE;
    }

    /**
     * Retourne le nombre de véhicules actuellement stockés.
     * 
     * @return le nombre de véhicules
     */
    public int getVehicleCount() {
        return vehicles.size();
    }

    /**
     * Méthode de callback JPA appelée avant la persistance
     */
    @PrePersist
    protected void onCreate() {
        // Validation basique des horaires
    }

    /**
     * Méthode de callback JPA appelée avant la mise à jour
     */
    @PreUpdate
    protected void onUpdate() {
        if (vehicles.size() > MAX_VEHICLES_PER_GARAGE) {
            throw new VehicleQuotaExceededException(this.id);
        }
    }
}
