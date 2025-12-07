package com.mercatosys.service.interfaces;

import com.mercatosys.dto.order.ClientOrderHistoryDTO;
import com.mercatosys.dto.order.ClientOrderStatsDTO;
import com.mercatosys.dto.order.OrderRequestDTO;
import com.mercatosys.dto.order.OrderResponseDTO;

import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(OrderRequestDTO dto);

    OrderResponseDTO confirmOrder(Long orderId);

    OrderResponseDTO cancelOrder(Long orderId);

    OrderResponseDTO getOrderById(Long id);

    List<OrderResponseDTO> getAllOrders();
    ClientOrderStatsDTO getClientOrderStats(Long clientId);
    List<ClientOrderHistoryDTO> getClientOrderHistory(Long clientId);
}
