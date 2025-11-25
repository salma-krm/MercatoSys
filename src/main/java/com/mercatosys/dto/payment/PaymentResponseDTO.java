package com.mercatosys.dto.payment;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentResponseDTO {

    private Long id;
    private String method;
    private double amount;
    private LocalDateTime paidAt;
}
