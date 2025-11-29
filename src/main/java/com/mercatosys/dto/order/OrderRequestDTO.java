package com.mercatosys.dto.order;

import com.mercatosys.dto.orderItem.OrderItemRequestDTO;
import com.mercatosys.dto.payment.PaymentRequestDTO;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderRequestDTO {

    @NotNull(message = "Le client est obligatoire")
    private Long clientId;

    private Long promoCodeId;

    @NotEmpty(message = "La commande doit contenir au moins un article")
    private List<OrderItemRequestDTO> items;
}
