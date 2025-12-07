package com.mercatosys.controller;

import com.mercatosys.dto.order.ClientOrderHistoryDTO;
import com.mercatosys.dto.order.ClientOrderStatsDTO;
import com.mercatosys.service.impl.SessionManager;
import com.mercatosys.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/me/orders")
@RequiredArgsConstructor
public class ClientOrderController {

    private final OrderService orderService;
    private final SessionManager sessionManager;

    @GetMapping("/stats")
    public ResponseEntity<ClientOrderStatsDTO> getStats(
            @RequestHeader("sessionId") String sessionId
    ) {
        Long clientId = sessionManager.getUserIdBySessionId(sessionId);

        if (clientId == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(orderService.getClientOrderStats(clientId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ClientOrderHistoryDTO>> getHistory(
            @RequestHeader("sessionId") String sessionId
    ) {
        Long clientId = sessionManager.getUserIdBySessionId(sessionId);

        if (clientId == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(orderService.getClientOrderHistory(clientId));
    }
}

