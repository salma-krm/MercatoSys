package com.mercatosys.repositories;

import com.mercatosys.entity.Product;
import com.mercatosys.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
