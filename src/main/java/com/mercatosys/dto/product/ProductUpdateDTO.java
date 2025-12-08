package com.mercatosys.dto.product;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductUpdateDTO {

    @NotBlank(message = "Le nom du produit est obligatoire")
    private String name;

    @Positive(message = "Le prix doit être > 0")
    private double price;

    @NotBlank(message = "La catégorie est obligatoire")
    private String category;
    private LocalDateTime updatedAt;
    @PositiveOrZero(message = "Le stock doit être >= 0")
    private int stock;
}