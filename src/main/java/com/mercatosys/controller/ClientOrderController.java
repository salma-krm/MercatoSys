package com.mercatosys.controller;

import com.mercatosys.dto.order.ClientOrderHistoryDTO;
import com.mercatosys.dto.order.ClientOrderStatsDTO;
import com.mercatosys.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients/{clientId}/orders")
@RequiredArgsConstructor
public class ClientOrderController {

    private final OrderService orderService;

    @GetMapping("/stats")
    public ResponseEntity<ClientOrderStatsDTO> getStats(@PathVariable Long clientId) {
        return ResponseEntity.ok(orderService.getClientOrderStats(clientId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ClientOrderHistoryDTO>> getHistory(@PathVariable Long clientId) {
        return ResponseEntity.ok(orderService.getClientOrderHistory(clientId));
    }
}
