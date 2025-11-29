package com.mercatosys.service.interfaces;

import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.client.ClientUpdateDTO;

import java.util.List;

public interface ClientService {

    ClientResponseDTO update(Long id, ClientRequestDTO dto);

    void delete(Long id);

    ClientResponseDTO getById(Long id);

    List<ClientResponseDTO> getAll();
}
