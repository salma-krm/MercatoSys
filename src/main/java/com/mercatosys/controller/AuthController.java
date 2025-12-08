package com.mercatosys.controller;


import com.mercatosys.annotation.RequireAuth;
import com.mercatosys.dto.user.LoginRequestDTO;
import com.mercatosys.dto.user.LoginResponseDTO;
import com.mercatosys.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;




@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.status(201).body(response);
    }

@RequireAuth
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Session-Id") String sessionId) {
        authService.logout(sessionId);
        return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
    }
}
