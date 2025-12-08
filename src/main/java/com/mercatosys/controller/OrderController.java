package com.mercatosys.controller;

import com.mercatosys.annotation.RequireAuth;
import com.mercatosys.annotation.RequireRole;
import com.mercatosys.dto.order.OrderRequestDTO;
import com.mercatosys.dto.order.OrderResponseDTO;
import com.mercatosys.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;



@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @RequireAuth
    @RequireRole("ADMIN")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }


    @RequireAuth
    @RequireRole("ADMIN")
    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDTO> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }


    @RequireAuth
    @RequireRole("ADMIN")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }


    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }


    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
