package com.mercatosys.dto.product;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductResponseDTO {

    private Long id;
    private String name;
    private double price;
    private String category;
    private int stock;
    private LocalDateTime createdAt;
}