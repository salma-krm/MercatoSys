package com.mercatosys.controller;

import com.mercatosys.annotation.RequireAuth;
import com.mercatosys.annotation.RequireRole;
import com.mercatosys.dto.payment.PaymentRequestDTO;
import com.mercatosys.dto.payment.PaymentResponseDTO;
import com.mercatosys.service.interfaces.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders/{orderId}/payments")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;

    @RequireAuth
    @RequireRole("ADMIN")
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> payOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody PaymentRequestDTO dto
    ) {
        PaymentResponseDTO response = paymentService.payOrder(orderId, dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
