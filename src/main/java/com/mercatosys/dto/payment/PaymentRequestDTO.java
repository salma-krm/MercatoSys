package com.mercatosys.dto.payment;


import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequestDTO {

    @NotBlank(message = "La méthode de paiement est obligatoire")
    private String method;

    @Positive(message = "Le montant doit être > 0")
    private double amount;
}
