package com.mercatosys.dto.promocode;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PromoCodeRequestDTO {

    @NotBlank(message = "Le code promo est obligatoire")
    @Pattern(
            regexp = "PROMO-[A-Z0-9]{4}",
            message = "Format invalide : doit être PROMO-XXXX"
    )
    private String code;

    @PositiveOrZero
    @Max(100)
    private Double discountPercentage;

    @PositiveOrZero
    private Double discountAmount;

    @Future
    private LocalDateTime expirationDate;

    private Boolean active = true;

    @PositiveOrZero
    private Double minimumOrderAmount;

    private Boolean used = false;
}
