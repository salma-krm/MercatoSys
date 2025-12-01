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
import com.mercatosys.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final OrderMapper orderMapper;

    @Value("${app.tva.rate}")
    private double tvaRate;

    @Transactional
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client " + dto.getClientId() + " introuvable"));

        PromoCode promoCode = null;
        if (dto.getPromoCodeId() != null) {
            promoCode = promoCodeRepository.findById(dto.getPromoCodeId())
                    .orElseThrow(() -> new ResourceNotFoundException("PromoCode " + dto.getPromoCodeId() + " introuvable"));
            if (!promoCode.getActive())
                throw new IllegalStateException("Le code promo est désactivé");
            if (orderRepository.existsByPromoCodeId(promoCode.getId()))
                throw new IllegalStateException("Le code promo a déjà été utilisé");
            if (promoCode.getExpirationDate().isBefore(LocalDateTime.now()))
                throw new IllegalStateException("Le code promo est expiré");
        }

        Order order = Order.builder()
                .client(client)
                .promoCode(promoCode)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        double totalHT = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit " + itemDTO.getProductId() + " introuvable"));

            if (product.getStock() < itemDTO.getQuantity()) {
                order.setStatus(OrderStatus.REJECTED);
                return orderMapper.toDTO(order);
            }

            product.setStock(product.getStock() - itemDTO.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(product.getPrice())
                    .totalPrice(product.getPrice() * itemDTO.getQuantity())
                    .build();

            totalHT += orderItem.getTotalPrice();
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        double discount = calculateLoyaltyDiscount(client.getLevel(), totalHT);

        if (promoCode != null) {
            if (promoCode.getDiscountPercentage() != null)
                discount += totalHT * (promoCode.getDiscountPercentage() / 100.0);
            if (promoCode.getDiscountAmount() != null)
                discount += promoCode.getDiscountAmount();
        }

        double totalAfterDiscount = totalHT - discount;
        double tva = totalAfterDiscount * tvaRate;
        double totalTTC = totalAfterDiscount + tva;

        order.setTotalHT(totalAfterDiscount);
        order.setDiscountApplied(discount);
        order.setTotalTTC(totalTTC);

        orderRepository.save(order);

        return orderMapper.toDTO(order);
    }

    @Transactional
    @Override
    public OrderResponseDTO confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande " + orderId + " introuvable"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("La commande n'est pas en statut PENDING");
        }

        double totalPaid = order.getPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        totalPaid = Math.round(totalPaid * 100.0) / 100.0;
        double totalTTC = Math.round(order.getTotalTTC() * 100.0) / 100.0;

        if (totalPaid < totalTTC) {
            throw new IllegalStateException("La commande n'est pas totalement payée");
        }

        order.setStatus(OrderStatus.CONFIRMED);

        Client client = order.getClient();
        client.setTotalOrder(client.getTotalOrder() + 1);
        updateCustomerLevel(client);
        clientRepository.save(client);

        orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    @Transactional
    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande " + orderId + " introuvable"));

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        return orderMapper.toDTO(order);
    }

    double calculateLoyaltyDiscount(CustomerLevel level, double totalHT) {
        return switch (level) {
            case SILVER -> totalHT >= 500 ? totalHT * 0.05 : 0;
            case GOLD -> totalHT >= 800 ? totalHT * 0.10 : 0;
            case PLATINUM -> totalHT >= 1200 ? totalHT * 0.15 : 0;
            default -> 0;
        };
    }

    @Transactional
    protected void updateCustomerLevel(Client client) {
        int totalOrders = client.getTotalOrder();
        Double totalSpent = orderRepository.sumConfirmedOrdersByClient(client.getId());
        if (totalSpent == null) totalSpent = 0.0;

        if (totalOrders >= 20 || totalSpent >= 15000)
            client.setLevel(CustomerLevel.PLATINUM);
        else if (totalOrders >= 10 || totalSpent >= 5000)
            client.setLevel(CustomerLevel.GOLD);
        else if (totalOrders >= 3 || totalSpent >= 1000)
            client.setLevel(CustomerLevel.SILVER);
        else
            client.setLevel(CustomerLevel.BASIC);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Commande " + id + " introuvable"));
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<OrderResponseDTO> list = new ArrayList<>();
        for (Order o : orderRepository.findAll()) {
            list.add(orderMapper.toDTO(o));
        }
        return list;
    }
}
