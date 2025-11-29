package com.mercatosys.repositories;

import com.mercatosys.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {


    boolean existsByCodeAndActiveTrue(String code);

    List<PromoCode> findAllByActiveTrue();
}
