package com.mercatosys.entity;

import com.mercatosys.enums.PaymentMethod;
import com.mercatosys.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Payment extends BaseEntity {



    private Integer paymentNumber;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentType;

    @Positive(message = "Le montant doit être > 0")
    private Double amount;

    private LocalDateTime paymentDate;

    private LocalDateTime depositDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String reference;

    private String bank;

}
