package com.renault.garage.domain.model;

import com.renault.garage.domain.model.enums.AccessoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entité représentant un accessoire de véhicule.
 * 
 * Un accessoire est associé à un véhicule et possède un nom,
 * une description, un prix et un type.
 * 
 * Pattern utilisé: Entity (DDD)
 * Principe SOLID: Single Responsibility Principle
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Entity
@Table(name = "accessories", indexes = {
    @Index(name = "idx_accessory_type", columnList = "type"),
    @Index(name = "idx_accessory_vehicle", columnList = "vehicle_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "vehicle")
@EqualsAndHashCode(of = "id")
public class Accessory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nom de l'accessoire (obligatoire)
     */
    @NotBlank(message = "Le nom de l'accessoire est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Description de l'accessoire (obligatoire)
     */
    @NotBlank(message = "La description de l'accessoire est obligatoire")
    @Size(min = 10, max = 500, message = "La description doit contenir entre 10 et 500 caractères")
    @Column(nullable = false, length = 500)
    private String description;

    /**
     * Prix de l'accessoire (obligatoire)
     */
    @NotNull(message = "Le prix de l'accessoire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être supérieur à 0")
    @Digits(integer = 10, fraction = 2, message = "Le prix doit avoir au maximum 10 chiffres entiers et 2 décimales")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    /**
     * Type de l'accessoire (obligatoire)
     */
    @NotNull(message = "Le type de l'accessoire est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccessoryType type;

    /**
     * Véhicule auquel appartient cet accessoire
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @NotNull(message = "L'accessoire doit être associé à un véhicule")
    private Vehicle vehicle;

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
     * Méthode de callback JPA appelée avant la persistance
     */
    @PrePersist
    protected void onCreate() {
        if (price != null && price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }
    }

    /**
     * Méthode de callback JPA appelée avant la mise à jour
     */
    @PreUpdate
    protected void onUpdate() {
        if (price != null && price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }
    }
}
