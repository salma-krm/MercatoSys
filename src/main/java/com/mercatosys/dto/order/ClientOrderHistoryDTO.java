package com.mercatosys.dto.order;

import java.time.LocalDateTime;

public record ClientOrderHistoryDTO(
        Long orderId,
        LocalDateTime createdAt,
        Double totalTTC,
        String status
) {}
