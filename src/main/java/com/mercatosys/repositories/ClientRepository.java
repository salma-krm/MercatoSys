package com.mercatosys.repositories;

import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUserId(Long userId);
}

