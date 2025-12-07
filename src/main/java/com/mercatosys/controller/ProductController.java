package com.mercatosys.controller;

import com.mercatosys.dto.product.ProductRequestDTO;
import com.mercatosys.dto.product.ProductResponseDTO;
import com.mercatosys.dto.product.ProductUpdateDTO;
import com.mercatosys.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "Gestion des produits")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Créer un produit",
            description = "Ajoute un nouveau produit dans le catalogue.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produit créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Validated @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(201).body(productService.create(dto));
    }

    @Operation(summary = "Récupérer un produit par ID",
            description = "Retourne les détails d’un produit spécifique.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit trouvé"),
            @ApiResponse(responseCode = "404", description = "Produit introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(summary = "Liste des produits",
            description = "Retourne la liste complète des produits disponibles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée")
    })
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @Operation(summary = "Mettre à jour un produit",
            description = "Modifie les informations d’un produit existant.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produit mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id,
                                                     @Validated @RequestBody ProductUpdateDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @Operation(summary = "Supprimer un produit",
            description = "Supprime un produit du catalogue.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produit supprimé"),
            @ApiResponse(responseCode = "404", description = "Produit introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
