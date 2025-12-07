package com.mercatosys.controller;

import com.mercatosys.dto.order.OrderRequestDTO;
import com.mercatosys.dto.order.OrderResponseDTO;
import com.mercatosys.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Commandes", description = "Gestion des commandes clients")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Créer une commande",
            description = "Crée une nouvelle commande avec les produits sélectionnés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commande créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @Operation(summary = "Confirmer une commande",
            description = "Marque une commande comme CONFIRMÉE après paiement complet.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commande confirmée"),
            @ApiResponse(responseCode = "404", description = "Commande introuvable"),
            @ApiResponse(responseCode = "400", description = "Commande non éligible à la confirmation")
    })
    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDTO> confirmOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @Operation(summary = "Annuler une commande",
            description = "Annule une commande et restaure le stock si elle était confirmée.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commande annulée"),
            @ApiResponse(responseCode = "404", description = "Commande introuvable")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @Operation(summary = "Récupérer une commande par ID",
            description = "Retourne les informations détaillées d'une commande.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commande trouvée"),
            @ApiResponse(responseCode = "404", description = "Commande introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Lister toutes les commandes",
            description = "Retourne la liste de toutes les commandes existantes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des commandes renvoyée")
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
