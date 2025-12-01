package com.renault.garage.domain.model;

import com.renault.garage.domain.model.enums.FuelType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un véhicule.
 * 
 * Un véhicule est associé à un garage et peut avoir plusieurs accessoires.
 * Il possède une marque, un modèle, une année de fabrication et un type de carburant.
 * 
 * Pattern utilisé: Entity (DDD)
 * Principe SOLID: Single Responsibility Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "idx_vehicle_brand", columnList = "brand"),
    @Index(name = "idx_vehicle_model", columnList = "model"),
    @Index(name = "idx_vehicle_fuel_type", columnList = "fuel_type"),
    @Index(name = "idx_vehicle_garage", columnList = "garage_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"garage", "accessories"})
@EqualsAndHashCode(of = "id")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Marque du véhicule (obligatoire)
     */
    @NotBlank(message = "La marque du véhicule est obligatoire")
    @Size(min = 2, max = 50, message = "La marque doit contenir entre 2 et 50 caractères")
    @Column(nullable = false, length = 50)
    private String brand;

    /**
     * Modèle du véhicule (obligatoire)
     */
    @NotBlank(message = "Le modèle du véhicule est obligatoire")
    @Size(min = 1, max = 50, message = "Le modèle doit contenir entre 1 et 50 caractères")
    @Column(nullable = false, length = 50)
    private String model;

    /**
     * Année de fabrication (obligatoire)
     */
    @NotNull(message = "L'année de fabrication est obligatoire")
    @Min(value = 1900, message = "L'année de fabrication doit être supérieure ou égale à 1900")
    @Max(value = 2100, message = "L'année de fabrication doit être inférieure ou égale à 2100")
    @Column(name = "year_of_manufacture", nullable = false)
    private Integer yearOfManufacture;

    /**
     * Type de carburant (obligatoire)
     */
    @NotNull(message = "Le type de carburant est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 20)
    private FuelType fuelType;

    /**
     * Numéro de série du véhicule (VIN - Vehicle Identification Number)
     */
    @Column(name = "vin", unique = true, length = 17)
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Le VIN doit contenir exactement 17 caractères alphanumériques")
    private String vin;

    /**
     * Couleur du véhicule
     */
    @Size(max = 30, message = "La couleur ne peut pas dépasser 30 caractères")
    @Column(length = 30)
    private String color;

    /**
     * Kilométrage du véhicule
     */
    @Min(value = 0, message = "Le kilométrage ne peut pas être négatif")
    @Column(name = "mileage")
    private Integer mileage;

    /**
     * Garage dans lequel est stocké le véhicule
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false)
    @NotNull(message = "Le véhicule doit être associé à un garage")
    private Garage garage;

    /**
     * Liste des accessoires du véhicule
     */
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Accessory> accessories = new ArrayList<>();

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
     * Ajoute un accessoire au véhicule.
     * 
     * @param accessory l'accessoire à ajouter
     */
    public void addAccessory(Accessory accessory) {
        accessories.add(accessory);
        accessory.setVehicle(this);
    }

    /**
     * Retire un accessoire du véhicule.
     * 
     * @param accessory l'accessoire à retirer
     */
    public void removeAccessory(Accessory accessory) {
        accessories.remove(accessory);
        accessory.setVehicle(null);
    }

    /**
     * Retourne le nombre d'accessoires du véhicule.
     * 
     * @return le nombre d'accessoires
     */
    public int getAccessoryCount() {
        return accessories.size();
    }

    /**
     * Vérifie si le véhicule est électrique ou hybride.
     * 
     * @return true si le véhicule est électrique ou hybride
     */
    public boolean isEcoFriendly() {
        return fuelType == FuelType.ELECTRIQUE || fuelType == FuelType.HYBRIDE;
    }

    /**
     * Retourne une représentation textuelle du véhicule.
     * 
     * @return brand model (year)
     */
    public String getDisplayName() {
        return String.format("%s %s (%d)", brand, model, yearOfManufacture);
    }

    /**
     * Méthode de callback JPA appelée avant la persistance
     */
    @PrePersist
    protected void onCreate() {
        validateYearOfManufacture();
    }

    /**
     * Méthode de callback JPA appelée avant la mise à jour
     */
    @PreUpdate
    protected void onUpdate() {
        validateYearOfManufacture();
    }

    /**
     * Valide que l'année de fabrication est cohérente
     */
    private void validateYearOfManufacture() {
        if (yearOfManufacture != null) {
            int currentYear = LocalDateTime.now().getYear();
            if (yearOfManufacture > currentYear + 1) {
                throw new IllegalArgumentException(
                    "L'année de fabrication ne peut pas être dans le futur"
                );
            }
        }
    }
}
