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
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @NotNull
    private boolean deleted;

    @PositiveOrZero(message = "Le pourcentage doit être >= 0")
    @Max(value = 100, message = "Le pourcentage ne peut pas dépasser 100")
    private Double discountPercentage;

    @PositiveOrZero(message = "Le montant de réduction doit être >= 0")
    private Double discountAmount;   // Optionnel selon UML, mais recommandé

    @Future(message = "La date d’expiration doit être future")
    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @NotNull
    private boolean active;

    @PositiveOrZero(message = "Le montant minimum doit être >= 0")
    private Double minimumOrderAmount; // Optionnel si tu veux minimum d'achat
}
