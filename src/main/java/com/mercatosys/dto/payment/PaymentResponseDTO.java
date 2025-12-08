package com.mercatosys.dto.payment;

import com.mercatosys.enums.PaymentMethod;
import com.mercatosys.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Long id;
    private Integer paymentNumber;
    private PaymentMethod paymentType;
    private Double amount;
    private LocalDateTime createdAt;
    private LocalDateTime paymentDate;
    private LocalDateTime depositDate;
    private PaymentStatus status;
    private String reference;
    private String bank;
    private Long orderId;
}
