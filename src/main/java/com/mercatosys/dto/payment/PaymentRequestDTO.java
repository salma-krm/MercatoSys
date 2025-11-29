package com.mercatosys.dto.payment;

import com.mercatosys.enums.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    @NotNull(message = "La méthode de paiement est obligatoire")
    private PaymentMethod paymentType;

    @Positive(message = "Le montant doit être > 0")
    private Double amount;

    private LocalDateTime paymentDate;

    private LocalDateTime depositDate;
    private String reference;
    private String bank;

    private Long orderId;
}
