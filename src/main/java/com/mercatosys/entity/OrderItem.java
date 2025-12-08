package com.mercatosys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "order_items")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
public class OrderItem  extends BaseEntity {



    @Positive(message = "La quantité doit être supérieure à 0")
    private int quantity;

    @Positive(message = "Le prix unitaire doit être > 0")
    private double unitPrice;

    @Positive(message = "Le total doit être > 0")
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
