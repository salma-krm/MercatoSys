package com.mercatosys.controller;

import com.mercatosys.dto.promocode.PromoCodeRequestDTO;
import com.mercatosys.dto.promocode.PromoCodeResponseDTO;
import com.mercatosys.dto.promocode.PromoCodeUpdateDTO;
import com.mercatosys.service.interfaces.PromoCodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promocodes")
@RequiredArgsConstructor
@Tag(name = "Promo Codes", description = "Management of promo codes")
public class PromoCodeController {

    private final PromoCodeService service;

    @PostMapping
    @Operation(
            summary = "Create a new promo code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Promo code created",
                            content = @Content(schema = @Schema(implementation = PromoCodeResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<PromoCodeResponseDTO> create(@Valid @RequestBody PromoCodeRequestDTO dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing promo code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Promo code updated",
                            content = @Content(schema = @Schema(implementation = PromoCodeResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Promo code not found")
            }
    )
    public ResponseEntity<PromoCodeResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PromoCodeUpdateDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get promo code by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Promo code found",
                            content = @Content(schema = @Schema(implementation = PromoCodeResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Promo code not found")
            }
    )
    public ResponseEntity<PromoCodeResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(
            summary = "Get all promo codes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of promo codes",
                            content = @Content(schema = @Schema(implementation = PromoCodeResponseDTO.class)))
            }
    )
    public ResponseEntity<List<PromoCodeResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete promo code by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Promo code deleted"),
                    @ApiResponse(responseCode = "404", description = "Promo code not found")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
