package com.mercatosys.repositories;

import com.mercatosys.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT COALESCE(SUM(o.totalTTC),0) FROM Order o WHERE o.client.id = :clientId AND o.status = 'CONFIRMED'")
    Double sumConfirmedOrdersByClient(Long clientId);

    boolean existsByPromoCodeId( Long promoCodeId);
}
