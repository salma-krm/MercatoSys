package com.mercatosys.entity;

import com.mercatosys.enums.PaymentMethod;
import com.mercatosys.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer paymentNumber; // numéro de paiement unique

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentType; // ESPÈCES, CHEQUE, VIREMENT

    @Positive(message = "Le montant doit être > 0")
    private Double amount;

    private LocalDateTime paymentDate; // date du paiement

    private LocalDateTime depositDate; // date de dépôt pour chèque/virement

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, RECEIVED

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order; // commande associée

    private String reference; // référence chèque ou virement

    private String bank; // banque du chèque ou virement

}
