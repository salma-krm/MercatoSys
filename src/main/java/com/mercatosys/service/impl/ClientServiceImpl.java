package com.mercatosys.service.impl;

import com.mercatosys.Exception.DuplicateResourceException;
import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.client.ClientUpdateDTO;
import com.mercatosys.entity.Client;
import com.mercatosys.entity.User;
import com.mercatosys.enums.CustomerLevel;
import com.mercatosys.enums.Role;
import com.mercatosys.mapper.ClientMapper;
import com.mercatosys.repositories.ClientRepository;
import com.mercatosys.repositories.UserRepository;
import com.mercatosys.service.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;


    @Transactional
    @Override
    public ClientResponseDTO create(ClientRequestDTO request) {

        if (userRepository.existsByEmail(request.getUser().getEmail())) {
            throw new DuplicateResourceException("This email is already in use");
        }

        User user = User.builder()
                .username(request.getUser().getUsername())
                .email(request.getUser().getEmail())
                .password(encodePassword(request.getUser().getPassword()))
                .role(Role.CLIENT)
                .build();

        User savedUser = userRepository.save(user);

        Client client = Client.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .user(savedUser)
                .level(CustomerLevel.BASIC)
                .totalOrder(0)
                .totalSpent(0.0)
                .build();

        Client savedClient = clientRepository.save(client);
        return clientMapper.toResponseDTO(savedClient);
    }
    @Override
    public ClientResponseDTO update(Long id, ClientUpdateDTO dto) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client"));

        clientMapper.updateEntityFromDTO(dto, client);

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


    private String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(password.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error encoding password");
        }
    }
}
