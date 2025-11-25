package com.mercatosys.dto.orderItem;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemResponseDTO {

    private Long id;
    private Long productId;
    private String productName;

    private double unitPrice;
    private int quantity;
    private double totalPrice;
}
