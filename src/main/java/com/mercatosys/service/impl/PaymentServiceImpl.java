package com.mercatosys.service.impl;

import com.mercatosys.Exception.InvalidPaymentException;
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

        double totalPaid = order.getPayments().stream().mapToDouble(Payment::getAmount).sum();
        double remaining = round(order.getTotalTTC() - totalPaid);

        if (remaining <= 0) {
            throw new InvalidPaymentException("Cette commande est déjà entièrement payée");
        }


        double requestedAmount = round(dto.getAmount());
        double amountToPay = requestedAmount > remaining ? remaining : requestedAmount;

        validatePayment(dto, amountToPay, remaining);

        Payment payment = buildPayment(order, dto, amountToPay);
        payment.setStatus(resolvePaymentStatus(dto, payment));

        Payment saved = paymentRepository.save(payment);

        updateOrderStatusIfFullyPaid(order, totalPaid + amountToPay);

        return toDTO(saved);
    }


    private void validatePayment(PaymentRequestDTO dto, double amount, double remaining) {
        if (amount <= 0) throw new IllegalArgumentException("Le montant doit être > 0");


        if (dto.getPaymentType() == PaymentMethod.ESPÈCES && amount > 20000) {
            throw new InvalidPaymentException("Le paiement en espèces ne peut dépasser 20,000 DH");
        }

    }

    private PaymentStatus resolvePaymentStatus(PaymentRequestDTO dto, Payment payment) {
        return switch (dto.getPaymentType()) {
            case ESPÈCES -> PaymentStatus.COMPLETED;
            case CHÈQUE -> PaymentStatus.PENDING;
            case VIREMENT -> (payment.getDepositDate() != null && !payment.getDepositDate().isAfter(LocalDateTime.now()))
                    ? PaymentStatus.COMPLETED
                    : PaymentStatus.PENDING;
        };
    }

    private Payment buildPayment(Order order, PaymentRequestDTO dto, double amount) {
        int nextPaymentNumber = order.getPayments().size() + 1;
        return Payment.builder()
                .order(order)
                .paymentType(dto.getPaymentType())
                .paymentNumber(nextPaymentNumber)
                .amount(amount)
                .paymentDate(dto.getPaymentDate() != null ? dto.getPaymentDate() : LocalDateTime.now())
                .depositDate(dto.getDepositDate())
                .reference(dto.getReference())
                .bank(dto.getBank())
                .status(PaymentStatus.PENDING)
                .build();
    }

    private void updateOrderStatusIfFullyPaid(Order order, double newTotalPaid) {
        if (round(newTotalPaid) >= order.getTotalTTC()) {
            order.setStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }
    }

    private PaymentResponseDTO toDTO(Payment p) {
        return PaymentResponseDTO.builder()
                .id(p.getId())

                .paymentNumber(p.getPaymentNumber())
                .paymentType(p.getPaymentType())
                .amount(p.getAmount())
                .paymentDate(p.getPaymentDate())
                .depositDate(p.getDepositDate())
                .status(p.getStatus())
                .reference(p.getReference())
                .bank(p.getBank())
                .orderId(p.getOrder().getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
