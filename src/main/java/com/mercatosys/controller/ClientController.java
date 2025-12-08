package com.mercatosys.controller;

import com.mercatosys.annotation.RequireAuth;
import com.mercatosys.annotation.RequireRole;
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



@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor

public class ClientController {

    private final ClientService clientService;

    @RequireAuth
    @RequireRole("ADMIN")
    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO dto) {
        ClientResponseDTO response = clientService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
@RequireAuth
@RequireRole("ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @RequestBody ClientUpdateDTO dto
    ) {
        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.getById(id);
        return ResponseEntity.ok(client);
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> clients = clientService.getAll();
        return ResponseEntity.ok(clients);
    }
    @RequireAuth
    @RequireRole("CLIENT")
    @GetMapping("/profile")
    public ResponseEntity<ClientResponseDTO> getProfile(
            @RequestHeader("sessionId") String sessionId
    ) {
        return ResponseEntity.ok(clientService.getProfile(sessionId));
    }


}
