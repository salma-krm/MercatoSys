package com.mercatosys.dto.order;

import com.mercatosys.dto.orderItem.OrderItemRequestDTO;
import com.mercatosys.dto.payment.PaymentRequestDTO;
import com.mercatosys.dto.payment.PaymentResponseDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderUpdateDTO {

    private Long promoCodeId;
    private LocalDateTime updatedAt;
    private List<OrderItemRequestDTO> items;
    private List<PaymentResponseDTO> payments;

}
