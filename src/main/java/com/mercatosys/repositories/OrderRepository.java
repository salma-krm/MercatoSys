package com.mercatosys.repositories;

import com.mercatosys.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByPromoCodeId(Long promoCodeId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.client.id = :clientId AND o.status = com.mercatosys.enums.OrderStatus.CONFIRMED")
    Long countConfirmedOrdersByClient(@Param("clientId") Long clientId);

    @Query("SELECT COALESCE(SUM(o.totalTTC),0) FROM Order o WHERE o.client.id = :clientId AND o.status = com.mercatosys.enums.OrderStatus.CONFIRMED")
    Double sumConfirmedOrdersByClient(@Param("clientId") Long clientId);

    @Query("SELECT MIN(o.createdAt) FROM Order o WHERE o.client.id = :clientId")
    LocalDateTime firstOrderDateByClient(@Param("clientId") Long clientId);

    @Query("SELECT MAX(o.createdAt) FROM Order o WHERE o.client.id = :clientId")
    LocalDateTime lastOrderDateByClient(@Param("clientId") Long clientId);

    List<Order> findByClientIdOrderByCreatedAtDesc(Long clientId);

}
