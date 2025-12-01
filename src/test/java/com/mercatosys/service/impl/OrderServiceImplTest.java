package com.mercatosys.service.impl;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.order.OrderRequestDTO;
import com.mercatosys.dto.order.OrderResponseDTO;
import com.mercatosys.dto.orderItem.OrderItemRequestDTO;
import com.mercatosys.entity.*;
import com.mercatosys.enums.CustomerLevel;
import com.mercatosys.enums.OrderStatus;
import com.mercatosys.mapper.OrderMapper;
import com.mercatosys.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client client;
    private Product product;
    private PromoCode promoCode;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .id(1L)
                .totalOrder(5)
                .level(CustomerLevel.BASIC)
                .build();

        product = Product.builder()
                .id(1L)
                .price(100.0)
                .stock(10)
                .build();

        promoCode = PromoCode.builder()
                .id(1L)
                .active(true)
                .discountPercentage(10.0)
                .expirationDate(LocalDateTime.now().plusDays(1))
                .build();
    }

    @Test
    void testCreateOrderSuccess() {
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setClientId(1L);
        dto.setItems(List.of(new OrderItemRequestDTO(1L, 2)));
        dto.setPromoCodeId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(promoCodeRepository.findById(1L)).thenReturn(Optional.of(promoCode));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(orderMapper.toDTO(any(Order.class))).thenAnswer(inv -> {
            Order o = (Order) inv.getArgument(0);
            return OrderResponseDTO.builder()
                    .id(o.getId())
                    .totalHT(o.getTotalHT())
                    .totalTTC(o.getTotalTTC())
                    .status(o.getStatus()) // enum مباشرة
                    .build();
        });

        OrderResponseDTO response = orderService.createOrder(dto);

        assertNotNull(response);
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertTrue(product.getStock() < 10);
    }

    @Test
    void testCreateOrderStockInsufficient() {
        product.setStock(1);
        OrderRequestDTO dto = new OrderRequestDTO();
        dto.setClientId(1L);
        dto.setItems(List.of(new OrderItemRequestDTO(1L, 5)));

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderMapper.toDTO(any(Order.class))).thenAnswer(inv -> {
            Order o = (Order) inv.getArgument(0);
            return OrderResponseDTO.builder().status(o.getStatus()).build();
        });

        OrderResponseDTO response = orderService.createOrder(dto);

        assertEquals(OrderStatus.REJECTED, response.getStatus());
    }

    @Test
    void testConfirmOrderSuccess() {
        Order order = Order.builder()
                .id(1L)
                .client(client)
                .status(OrderStatus.PENDING)
                .totalTTC(200.0)
                .payments(List.of(Payment.builder().amount(200.0).build()))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));
        when(clientRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));
        when(orderMapper.toDTO(any(Order.class))).thenAnswer(inv -> {
            Order o = (Order) inv.getArgument(0);
            return OrderResponseDTO.builder().status(o.getStatus()).build();
        });

        OrderResponseDTO response = orderService.confirmOrder(1L);

        assertEquals(OrderStatus.CONFIRMED, response.getStatus());
        assertEquals(6, client.getTotalOrder());
    }

    @Test
    void testConfirmOrderNotPending() {
        Order order = Order.builder()
                .id(1L)
                .status(OrderStatus.CANCELED)
                .build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Exception ex = assertThrows(IllegalStateException.class, () -> orderService.confirmOrder(1L));
        assertTrue(ex.getMessage().contains("La commande n'est pas en statut PENDING"));
    }

    @Test
    void testCancelOrderRestoresStock() {
        OrderItem item = OrderItem.builder().product(product).quantity(2).build();
        Order order = Order.builder()
                .id(1L)
                .status(OrderStatus.CONFIRMED)
                .items(List.of(item))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));
        when(orderMapper.toDTO(any(Order.class))).thenAnswer(inv -> {
            Order o = (Order) inv.getArgument(0);
            return OrderResponseDTO.builder().status(o.getStatus()).build();
        });

        OrderResponseDTO response = orderService.cancelOrder(1L);

        assertEquals(OrderStatus.CANCELED, response.getStatus());
        assertEquals(12, product.getStock()); // stock restored
    }

    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
        assertTrue(ex.getMessage().contains("Commande 1 introuvable"));
    }
}
