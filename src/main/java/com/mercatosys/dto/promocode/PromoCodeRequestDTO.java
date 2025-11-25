package com.mercatosys.dto.promocode;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PromoCodeRequestDTO {

    @NotBlank(message = "Le code promo est obligatoire")
    private String code;

    @PositiveOrZero(message = "Le pourcentage doit être >= 0")
    @Max(value = 100, message = "Le pourcentage ne peut pas dépasser 100")
    private Double discountPercentage;

    @PositiveOrZero(message = "Le montant de réduction doit être >= 0")
    private Double discountAmount;

    @Future(message = "La date d’expiration doit être future")
    private LocalDateTime expirationDate;

    private boolean active;

    @PositiveOrZero(message = "Le montant minimum doit être >= 0")
    private Double minimumOrderAmount;
}
