package com.mercatosys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @SuperBuilder
public class Product extends BaseEntity {



    @NotBlank(message = "Le nom du produit est obligatoire")
    private String name;

    @Positive(message = "Le prix doit être > 0")
    private double price;

    @NotBlank(message = "La catégorie est obligatoire")
    private String category;
    @NotNull
    @Builder.Default
    private Boolean active = true;
    @PositiveOrZero(message = "Le stock doit être >= 0")
    private int stock;
}
