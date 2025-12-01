package com.mercatosys.dto.order;

import com.mercatosys.dto.orderItem.OrderItemResponseDTO;
import com.mercatosys.dto.payment.PaymentResponseDTO;
import com.mercatosys.dto.promocode.PromoCodeResponseDTO;
import com.mercatosys.enums.OrderStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponseDTO {

    private Long id;
    private LocalDateTime createdAt;
    private OrderStatus status;

    private double totalHT;
    private double totalTTC;
    private double discountApplied;

    private PromoCodeResponseDTO promoCode;

    private Long clientId;

    private List<OrderItemResponseDTO> items;
    private List<PaymentResponseDTO> payments;
}
