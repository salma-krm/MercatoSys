package com.mercatosys.controller;

import com.mercatosys.annotation.RequireAuth;
import com.mercatosys.annotation.RequireRole;
import com.mercatosys.dto.promocode.PromoCodeRequestDTO;
import com.mercatosys.dto.promocode.PromoCodeResponseDTO;
import com.mercatosys.dto.promocode.PromoCodeUpdateDTO;
import com.mercatosys.service.interfaces.PromoCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/promocodes")
@RequiredArgsConstructor

public class PromoCodeController {

    private final PromoCodeService service;

    @RequireAuth
    @RequireRole("ADMIN")
    @PostMapping
    public ResponseEntity<PromoCodeResponseDTO> create(@Valid @RequestBody PromoCodeRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @PutMapping("/{id}")

    public ResponseEntity<PromoCodeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PromoCodeUpdateDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<PromoCodeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping
    public ResponseEntity<List<PromoCodeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @RequireAuth
    @RequireRole("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
