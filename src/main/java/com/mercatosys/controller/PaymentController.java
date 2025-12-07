package com.mercatosys.controller;

import com.mercatosys.dto.payment.PaymentRequestDTO;
import com.mercatosys.dto.payment.PaymentResponseDTO;
import com.mercatosys.service.interfaces.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/orders/{orderId}/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Manage payments for orders")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(
            summary = "Pay an order",
            description = "Create a payment for a specific order. Handles partial or full payments.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Payment created successfully",
                            content = @Content(schema = @Schema(implementation = PaymentResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid payment request"),
                    @ApiResponse(responseCode = "404", description = "Order not found")
            }
    )
    public ResponseEntity<PaymentResponseDTO> payOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody PaymentRequestDTO dto
    ) {
        PaymentResponseDTO response = paymentService.payOrder(orderId, dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
