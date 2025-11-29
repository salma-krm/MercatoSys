package com.mercatosys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "promo_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le code promo est obligatoire")
    @Pattern(regexp = "PROMO-[A-Z0-9]{4}", message = "Format invalide : doit être PROMO-XXXX")
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @PositiveOrZero(message = "Le pourcentage doit être >= 0")
    @Max(value = 100, message = "Le pourcentage ne peut pas dépasser 100")
    private Double discountPercentage;

    @PositiveOrZero(message = "Le montant de réduction doit être >= 0")
    private Double discountAmount;

    @Future(message = "La date d’expiration doit être future")
    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @NotNull
    @Builder.Default
    private Boolean active = true; // ✅ soft delete avec ce champ

    @PositiveOrZero(message = "Le montant minimum doit être >= 0")
    private Double minimumOrderAmount;

    @NotNull
    @Builder.Default
    private Boolean used = false;
}
