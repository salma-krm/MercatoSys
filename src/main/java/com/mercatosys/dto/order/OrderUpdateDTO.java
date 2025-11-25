package com.mercatosys.dto.order;

import com.mercatosys.dto.orderItem.OrderItemRequestDTO;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class OrderUpdateDTO {

    private Long promoCodeId;

    private List<OrderItemRequestDTO> items;

    private List<PaymentRequestDTO> payments;
}
