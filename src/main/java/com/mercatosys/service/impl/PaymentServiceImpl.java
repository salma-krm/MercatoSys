package com.mercatosys.service.impl;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.payment.PaymentRequestDTO;
import com.mercatosys.dto.payment.PaymentResponseDTO;
import com.mercatosys.entity.Order;
import com.mercatosys.entity.Payment;
import com.mercatosys.enums.PaymentMethod;
import com.mercatosys.enums.PaymentStatus;
import com.mercatosys.enums.OrderStatus;
import com.mercatosys.repositories.OrderRepository;
import com.mercatosys.repositories.PaymentRepository;
import com.mercatosys.service.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public PaymentResponseDTO payOrder(Long orderId, PaymentRequestDTO dto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande " + orderId + " introuvable"));

        double totalTTC = order.getTotalTTC();
        double totalPaid = order.getPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        double remainingAmount = Math.round((totalTTC - totalPaid) * 100.0) / 100.0;
        double paymentAmount = Math.round(dto.getAmount() * 100.0) / 100.0;

        if (paymentAmount <= 0) {
            throw new IllegalArgumentException("Le montant du paiement doit être supérieur à 0");
        }

        if (paymentAmount > remainingAmount) {
            throw new IllegalArgumentException("Le montant dépasse le restant dû (" + remainingAmount + " DH)");
        }

        if (dto.getPaymentType() == PaymentMethod.ESPÈCES && paymentAmount > 20000) {
            throw new IllegalArgumentException("Le paiement en espèces ne peut dépasser 20,000 DH");
        }

        Payment payment = Payment.builder()
                .order(order)
                .paymentType(dto.getPaymentType())
                .amount(paymentAmount)
                .paymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDateTime.now())
                .depositDate(dto.getDepositDate())
                .reference(dto.getReference())
                .bank(dto.getBank())
                .status(PaymentStatus.PENDING)
                .build();

        switch (dto.getPaymentType()) {
            case ESPÈCES -> payment.setStatus(PaymentStatus.COMPLETED);
            case CHÈQUE -> payment.setStatus(PaymentStatus.PENDING);
            case VIREMENT -> {
                if (dto.getDepositDate() != null && !dto.getDepositDate().isAfter(LocalDateTime.now())) {
                    payment.setStatus(PaymentStatus.COMPLETED);
                } else {
                    payment.setStatus(PaymentStatus.PENDING);
                }
            }
        }

        Payment savedPayment = paymentRepository.save(payment);

        totalPaid += paymentAmount;
        totalPaid = Math.round(totalPaid * 100.0) / 100.0;

        if (totalPaid >= totalTTC) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        return PaymentResponseDTO.builder()
                .id(savedPayment.getId())
                .paymentNumber(savedPayment.getPaymentNumber())
                .paymentType(savedPayment.getPaymentType())
                .amount(savedPayment.getAmount())
                .paymentDate(savedPayment.getPaymentDate())
                .depositDate(savedPayment.getDepositDate())
                .status(savedPayment.getStatus())
                .reference(savedPayment.getReference())
                .bank(savedPayment.getBank())
                .orderId(order.getId())
                .build();
    }
}
