package com.mercatosys.service;

import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.client.ClientUpdateDTO;

import java.util.List;

public interface ClientService {

    ClientResponseDTO create(ClientRequestDTO dto);

    ClientResponseDTO update(Long id, ClientUpdateDTO dto);

    void delete(Long id);

    ClientResponseDTO getById(Long id);

    List<ClientResponseDTO> getAll();
}
