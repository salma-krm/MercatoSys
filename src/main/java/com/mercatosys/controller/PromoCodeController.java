package com.mercatosys.controller;

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
@RequestMapping("/api/v1/promocodes")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService service;


    @PostMapping
    public ResponseEntity<PromoCodeResponseDTO> create(@Valid @RequestBody PromoCodeRequestDTO dto) {
        PromoCodeResponseDTO created = service.create(dto);
        return ResponseEntity.ok(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PromoCodeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PromoCodeUpdateDTO dto
    ) {
        PromoCodeResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // ============================
    // 🔹 GET PROMO CODE BY ID
    // ============================
    @GetMapping("/{id}")
    public ResponseEntity<PromoCodeResponseDTO> getById(@PathVariable Long id) {
        PromoCodeResponseDTO promo = service.getById(id);
        return ResponseEntity.ok(promo);
    }


    @GetMapping
    public ResponseEntity<List<PromoCodeResponseDTO>> getAll() {
        List<PromoCodeResponseDTO> promos = service.getAll();
        return ResponseEntity.ok(promos);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
