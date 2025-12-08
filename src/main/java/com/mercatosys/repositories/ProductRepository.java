package com.mercatosys.repositories;

import com.mercatosys.entity.Product;
import com.mercatosys.entity.User;
import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceGreaterThan(double price);
}
