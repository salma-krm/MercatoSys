package com.mercatosys.dto.payment;


import com.mercatosys.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequestDTO {

    @NotBlank(message = "La méthode de paiement est obligatoire")
    private String method;

    @Positive(message = "Le montant doit être > 0")
    private double amount;
    @NotNull(message = "La méthode de paiement est obligatoire")
    private PaymentMethod methode ;
    private LocalDateTime paymentDate ;
}
