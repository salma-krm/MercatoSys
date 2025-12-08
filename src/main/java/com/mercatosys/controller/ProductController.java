package com.mercatosys.controller;

import com.mercatosys.annotation.RequireAuth;
import com.mercatosys.annotation.RequireRole;
import com.mercatosys.dto.product.ProductRequestDTO;
import com.mercatosys.dto.product.ProductResponseDTO;
import com.mercatosys.dto.product.ProductUpdateDTO;
import com.mercatosys.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;



@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor

public class ProductController {

    private final ProductService productService;


    @RequireAuth
    @RequireRole("ADMIN")
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Validated @RequestBody ProductRequestDTO dto) {
        return ResponseEntity.status(201).body(productService.create(dto));
    }


    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }


    @RequireAuth
    @RequireRole("ADMIN")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<ProductResponseDTO> products = productService.getAll(pageable);
        return ResponseEntity.ok(products);
    }



    @RequireAuth
    @RequireRole("ADMIN")

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id,
                                                     @Validated @RequestBody ProductUpdateDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }


    @RequireAuth
    @RequireRole("ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
