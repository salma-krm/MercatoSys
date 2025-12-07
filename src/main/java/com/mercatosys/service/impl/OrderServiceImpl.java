package com.mercatosys.service.impl;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.order.ClientOrderHistoryDTO;
import com.mercatosys.dto.order.ClientOrderStatsDTO;
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
import org.springframework.web.bind.annotation.RequestHeader;

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

        Client client = fetchClient(dto.getClientId());
        PromoCode promoCode = validatePromoCode(dto.getPromoCodeId());

        Order order = initOrder(client, promoCode);
        List<OrderItem> items = processOrderItems(dto, order);
        order.setItems(items);

        double totalHT = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();

        double discount = calculateTotalDiscount(client, promoCode, totalHT);
        double totalAfterDiscount = totalHT - discount;

        setOrderTotals(order, totalAfterDiscount, discount);

        orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    @Transactional
    @Override
    public OrderResponseDTO confirmOrder(Long orderId) {
        Order order = fetchOrder(orderId);

        if (order.getStatus() != OrderStatus.PENDING)
            throw new IllegalStateException("La commande n'est pas en statut PENDING");

        validateFullPayment(order);

        order.setStatus(OrderStatus.CONFIRMED);

        updateClientLevel(order.getClient());
        clientRepository.save(order.getClient());

        orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    @Transactional
    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {
        Order order = fetchOrder(orderId);

        if (order.getStatus() == OrderStatus.CONFIRMED)
            restoreStock(order);

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        return orderMapper.toDTO(order);
    }

    private Client fetchClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client " + id + " introuvable"));
    }

    private Order fetchOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande " + id + " introuvable"));
    }

    private PromoCode validatePromoCode(Long promoCodeId) {
        if (promoCodeId == null) return null;

        PromoCode promo = promoCodeRepository.findById(promoCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("PromoCode " + promoCodeId + " introuvable"));

        if (!promo.getActive())
            throw new IllegalStateException("Le code promo est désactivé");

        if (promo.getExpirationDate().isBefore(LocalDateTime.now()))
            throw new IllegalStateException("Le code promo est expiré");

        if (orderRepository.existsByPromoCodeId(promo.getId()))
            throw new IllegalStateException("Le code promo a déjà été utilisé");

        return promo;
    }

    private Order initOrder(Client client, PromoCode promoCode) {
        return Order.builder()
                .client(client)
                .promoCode(promoCode)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
    }

    private List<OrderItem> processOrderItems(OrderRequestDTO dto, Order order) {
        List<OrderItem> items = new ArrayList<>();

        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            Product product = fetchProduct(itemDTO.getProductId());
            validateStock(product, itemDTO.getQuantity());
            decreaseStock(product, itemDTO.getQuantity());

            items.add(buildOrderItem(order, product, itemDTO.getQuantity()));
        }

        return items;
    }

    private Product fetchProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit " + id + " introuvable"));
    }

    private void validateStock(Product product, int quantity) {
        if (product.getStock() < quantity)
            throw new IllegalStateException("Stock insuffisant pour le produit : " + product.getId());
    }

    private void decreaseStock(Product product, int quantity) {
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    private OrderItem buildOrderItem(Order order, Product product, int qty) {
        double totalPrice = product.getPrice() * qty;

        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(qty)
                .unitPrice(product.getPrice())
                .totalPrice(totalPrice)
                .build();
    }

    private double calculateTotalDiscount(Client client, PromoCode promo, double totalHT) {
        double discount = calculateLoyaltyDiscount(client.getLevel(), totalHT);

        if (promo != null) {
            if (promo.getDiscountPercentage() != null)
                discount += totalHT * promo.getDiscountPercentage() / 100.0;

            if (promo.getDiscountAmount() != null)
                discount += promo.getDiscountAmount();
        }

        return discount;
    }

    private void setOrderTotals(Order order, double totalAfterDiscount, double discount) {
        double tva = totalAfterDiscount * tvaRate;
        double totalTTC = totalAfterDiscount + tva;

        order.setTotalHT(totalAfterDiscount);
        order.setDiscountApplied(discount);
        order.setTotalTTC(totalTTC);
    }

    private void validateFullPayment(Order order) {
        double paid = order.getPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        if (round(paid) < round(order.getTotalTTC()))
            throw new IllegalStateException("La commande n'est pas totalement payée");
    }

    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            Product p = item.getProduct();
            p.setStock(p.getStock() + item.getQuantity());
            productRepository.save(p);
        }
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = fetchOrder(id);
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Transactional
    protected void updateClientLevel(Client client) {
        int orders = client.getTotalOrder();
        Double totalSpent = orderRepository.sumConfirmedOrdersByClient(client.getId());
        totalSpent = totalSpent == null ? 0.0 : totalSpent;

        if (orders >= 20 || totalSpent >= 15000)
            client.setLevel(CustomerLevel.PLATINUM);
        else if (orders >= 10 || totalSpent >= 5000)
            client.setLevel(CustomerLevel.GOLD);
        else if (orders >= 3 || totalSpent >= 1000)
            client.setLevel(CustomerLevel.SILVER);
        else
            client.setLevel(CustomerLevel.BASIC);
    }

    private double calculateLoyaltyDiscount(CustomerLevel level, double totalHT) {
        return switch (level) {
            case SILVER -> totalHT >= 500 ? totalHT * 0.05 : 0;
            case GOLD -> totalHT >= 800 ? totalHT * 0.10 : 0;
            case PLATINUM -> totalHT >= 1200 ? totalHT * 0.15 : 0;
            default -> 0;
        };
    }


    @Override
    public ClientOrderStatsDTO getClientOrderStats(Long clientId) {
        Long totalOrders = orderRepository.countConfirmedOrdersByClient(clientId);
        Double totalAmount = orderRepository.sumConfirmedOrdersByClient(clientId);
        LocalDateTime firstOrder = orderRepository.firstOrderDateByClient(clientId);
        LocalDateTime lastOrder = orderRepository.lastOrderDateByClient(clientId);

        return new ClientOrderStatsDTO(totalOrders, totalAmount, firstOrder, lastOrder);
    }

    @Override
    public List<ClientOrderHistoryDTO> getClientOrderHistory(Long clientId) {
        List<Order> orders = orderRepository.findByClientIdOrderByCreatedAtDesc(clientId);
        return orders.stream()
                .map(o -> new ClientOrderHistoryDTO(
                        o.getId(),
                        o.getCreatedAt(),
                        o.getTotalTTC(),
                        o.getStatus().name()
                ))
                .toList();
    }


}
