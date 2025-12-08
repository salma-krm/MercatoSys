package com.mercatosys.dto.client;

import com.mercatosys.enums.CustomerLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseDTO {

    private Long id;

    private String name;
    private String phone;
    private String address;
    private Long userId;
    private int totalOrder;
    private double totalSpent;
    private CustomerLevel level;
}
