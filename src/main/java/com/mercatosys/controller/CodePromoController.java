package com.mercatosys.controller;

import com.mercatosys.dto.promocode.*;

import com.mercatosys.service.interfaces.PromoCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promocodes")
@RequiredArgsConstructor
public class CodePromoController  {

    private final PromoCodeService service;

    @PostMapping
    public PromoCodeResponseDTO create(@Valid @RequestBody PromoCodeRequestDTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public PromoCodeResponseDTO update(@PathVariable Long id,
                                       @Valid @RequestBody PromoCodeUpdateDTO dto) {
        return service.update(id, dto);
    }

    @GetMapping("/{id}")
    public PromoCodeResponseDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<PromoCodeResponseDTO> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
