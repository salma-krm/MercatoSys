package com.mercatosys.dto.orderItem;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemRequestDTO {

    @NotNull(message = "Le produit est obligatoire")
    private Long productId;

    @Positive(message = "La quantité doit être > 0")
    private int quantity;
}
