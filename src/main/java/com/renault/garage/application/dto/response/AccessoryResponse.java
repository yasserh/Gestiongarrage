package com.renault.garage.application.dto.response;

import com.renault.garage.domain.model.enums.AccessoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de réponse pour un accessoire.
 * 
 * Pattern utilisé: DTO Pattern
 * 
 * @author Renault Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessoryResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private AccessoryType type;
    private Long vehicleId;
    private String vehicleDisplayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
