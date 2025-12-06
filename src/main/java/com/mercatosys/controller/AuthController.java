package com.mercatosys.controller;

import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.user.LoginRequestDTO;
import com.mercatosys.dto.user.LoginResponseDTO;
import com.mercatosys.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

// --- Swagger imports ---
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Gestion de l'authentification")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Inscription d'un nouveau client",
            description = "Créer un compte client et retourne les informations créées.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/register")
    public ResponseEntity<ClientResponseDTO> register(@Valid @RequestBody ClientRequestDTO request) {
        ClientResponseDTO response = authService.register(request);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Connexion utilisateur",
            description = "Retourne le token ou la session après authentification.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Connexion réussie"),
            @ApiResponse(responseCode = "401", description = "Identifiants incorrects")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Déconnexion utilisateur",
            description = "Ferme la session active de l'utilisateur.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Déconnexion réussie"),
            @ApiResponse(responseCode = "400", description = "Session inexistante")
    })
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Session-Id") String sessionId) {
        authService.logout(sessionId);
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }
}
