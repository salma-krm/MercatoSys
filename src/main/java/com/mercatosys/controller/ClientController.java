package com.mercatosys.controller;

import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.client.ClientUpdateDTO;
import com.mercatosys.service.interfaces.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Gestion des clients")
public class ClientController {

    private final ClientService clientService;

    @Operation(summary = "Créer un client", description = "Ajoute un nouveau client et retourne ses informations.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO dto) {
        ClientResponseDTO response = clientService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Mettre à jour un client", description = "Modifie les informations d'un client existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client mis à jour"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @RequestBody ClientUpdateDTO dto
    ) {
        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @Operation(summary = "Supprimer un client", description = "Supprime un client par son ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client supprimé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Récupérer un client par ID", description = "Retourne les détails d'un client.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client trouvé"),
            @ApiResponse(responseCode = "404", description = "Client introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    @Operation(summary = "Liste des clients", description = "Retourne tous les clients enregistrés.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée")
    })
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> clients = clientService.getAll();
        return ResponseEntity.ok(clients);
    }
}
