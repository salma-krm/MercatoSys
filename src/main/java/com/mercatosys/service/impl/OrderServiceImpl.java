package com.mercatosys.service.impl;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.order.OrderRequestDTO;
import com.mercatosys.dto.order.OrderResponseDTO;
import com.mercatosys.dto.orderItem.OrderItemRequestDTO;
import com.mercatosys.dto.payment.PaymentRequestDTO;
import com.mercatosys.entity.*;
import com.mercatosys.enums.CustomerLevel;
import com.mercatosys.enums.OrderStatus;
import com.mercatosys.enums.PaymentMethod;
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
    private final PaymentRepository paymentRepository;

    private final OrderMapper orderMapper;

    // TVA fixe depuis application.properties
    @Value("${app.tva.rate}")
    private double tvaRate; // exemple 0.20

    // ----------------- CREATE ORDER -----------------
    @Transactional
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto) {

        // 1️⃣ Vérifier client
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client " + dto.getClientId() + " introuvable"));

        // 2️⃣ Vérifier code promo si fourni
        PromoCode promoCode = null;
        if (dto.getPromoCodeId() != null) {
            promoCode = promoCodeRepository.findById(dto.getPromoCodeId())
                    .orElseThrow(() -> new ResourceNotFoundException("PromoCode " + dto.getPromoCodeId() + " introuvable"));
            if (!promoCode.isActive()) {
                throw new IllegalStateException("Le code promo est désactivé");
            }
        }

        // 3️⃣ Créer la commande
        Order order = Order.builder()
                .client(client)
                .promoCode(promoCode)
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .items(new ArrayList<>())
                .payments(new ArrayList<>())
                .build();

        double totalHT = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        // 4️⃣ Vérifier stock et créer les OrderItems
        for (OrderItemRequestDTO itemDTO : dto.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produit " + itemDTO.getProductId() + " introuvable"));

            if (product.getStock() < itemDTO.getQuantity()) {
                order.setStatus(OrderStatus.REJECTED);
                return orderMapper.toDTO(order); // stock insuffisant
            }

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

        // 5️⃣ Calcul remises
        double discount = 0;

        // Remise fidélité selon niveau client (applique seulement si ce n’est pas la 1ère commande)
        if (client.getTotalOrder() > 0) {
            discount += calculateLoyaltyDiscount(client.getLevel(), totalHT);
        }

        // Code promo
        if (promoCode != null) {
            if (promoCode.getDiscountPercentage() != null) {
                discount += totalHT * (promoCode.getDiscountPercentage() / 100.0);
            }
            if (promoCode.getDiscountAmount() != null) {
                discount += promoCode.getDiscountAmount();
            }
        }

        double totalAfterDiscount = totalHT - discount;
        double tva = totalAfterDiscount * tvaRate;
        double totalTTC = totalAfterDiscount + tva;

        order.setTotalHT(totalAfterDiscount);
        order.setDiscountApplied(discount);
        order.setTotalTTC(totalTTC);

        // 6️⃣ Paiements
        List<Payment> payments = new ArrayList<>();
        if (dto.getPayments() != null) {
            for (PaymentRequestDTO pay : dto.getPayments()) {
                Payment payment = Payment.builder()
                        .order(order)
                        .amount(pay.getAmount())
                        .method(PaymentMethod.valueOf(pay.getMethod().toUpperCase()))
                        .paymentDate(LocalDateTime.now())
                        .build();
                payments.add(payment);
            }
        }
        order.setPayments(payments);

        orderRepository.save(order);

        return orderMapper.toDTO(order);
    }

    // ----------------- CONFIRM ORDER -----------------
    @Transactional
    @Override
    public OrderResponseDTO confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande " + orderId + " introuvable"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("La commande n'est pas en statut PENDING");
        }

        // Vérifier que la commande est totalement payée
        double totalPaid = order.getPayments().stream().mapToDouble(Payment::getAmount).sum();
        if (totalPaid < order.getTotalTTC()) {
            throw new IllegalStateException("La commande n'est pas totalement payée");
        }

        // Décrémenter stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CONFIRMED);

        // Mettre à jour statistiques client
        Client client = order.getClient();
        client.setTotalOrder(client.getTotalOrder() + 1);
        updateCustomerLevel(client);
        clientRepository.save(client);

        orderRepository.save(order);
        return orderMapper.toDTO(order);
    }

    // ----------------- CANCEL ORDER -----------------
    @Transactional
    @Override
    public OrderResponseDTO cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande " + orderId + " introuvable"));

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            // Rendre le stock
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

    // ----------------- UTILS -----------------
    private double calculateLoyaltyDiscount(CustomerLevel level, double totalHT) {
        switch (level) {
            case SILVER:
                return totalHT >= 500 ? totalHT * 0.05 : 0;
            case GOLD:
                return totalHT >= 800 ? totalHT * 0.10 : 0;
            case PLATINUM:
                return totalHT >= 1200 ? totalHT * 0.15 : 0;
            default:
                return 0;
        }
    }

    @Transactional
    protected void updateCustomerLevel(Client client) {
        int totalOrders = client.getTotalOrder(); // عدد الطلبات المؤكدة

        // 1️⃣ حساب إجمالي المبلغ المصروف للعميل من كل الطلبات المؤكدة
        Double totalSpent = orderRepository.sumConfirmedOrdersByClient(client.getId());
        if (totalSpent == null) totalSpent = 0.0;

        // 2️⃣ تحديث مستوى العميل حسب القواعد
        if (totalOrders >= 20 || totalSpent >= 15000) {
            client.setLevel(CustomerLevel.PLATINUM);
        } else if (totalOrders >= 10 || totalSpent >= 5000) {
            client.setLevel(CustomerLevel.GOLD);
        } else if (totalOrders >= 3 || totalSpent >= 1000) {
            client.setLevel(CustomerLevel.SILVER);
        } else {
            client.setLevel(CustomerLevel.BASIC);
        }

        // 3️⃣ تحديث العميل في قاعدة البيانات
        clientRepository.save(client);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande " + id + " introuvable"));
        return orderMapper.toDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponseDTO> list = new ArrayList<>();
        for (Order o : orders) {
            list.add(orderMapper.toDTO(o));
        }
        return list;
    }
}
