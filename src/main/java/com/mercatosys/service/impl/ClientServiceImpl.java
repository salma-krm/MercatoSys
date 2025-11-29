package com.mercatosys.service.impl;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.client.ClientUpdateDTO;
import com.mercatosys.entity.Client;
import com.mercatosys.entity.User;
import com.mercatosys.mapper.ClientMapper;
import com.mercatosys.repositories.ClientRepository;
import com.mercatosys.repositories.UserRepository;
import com.mercatosys.service.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;



    @Override
    public ClientResponseDTO update(Long id, ClientRequestDTO dto) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client"));

        clientMapper.updateEntityFromDTO( dto, client);

        client = clientRepository.save(client);

        return clientMapper.toResponseDTO(client);
    }

    @Override
    public void delete(Long id) {

        if (!clientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Client");
        }

        clientRepository.deleteById(id);
    }

    @Override
    public ClientResponseDTO getById(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client"));

        return clientMapper.toResponseDTO(client);
    }

    @Override
    public List<ClientResponseDTO> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponseDTO)
                .toList();
    }
}
