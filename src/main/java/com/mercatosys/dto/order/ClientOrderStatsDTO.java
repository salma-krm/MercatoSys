package com.mercatosys.dto.order;

import java.time.LocalDateTime;

public record ClientOrderStatsDTO(
        Long totalConfirmedOrders,
        Double totalAmount,
        LocalDateTime firstOrderDate,
        LocalDateTime lastOrderDate
) {}
