package com.mercatosys.service.impl;

import com.mercatosys.Exception.DuplicateResourceException;
import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.user.LoginRequestDTO;
import com.mercatosys.entity.Client;
import com.mercatosys.entity.User;
import com.mercatosys.enums.Role;
import com.mercatosys.mapper.ClientMapper;
import com.mercatosys.repositories.ClientRepository;
import com.mercatosys.repositories.UserRepository;
import com.mercatosys.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final SessionManager sessionManager;

    @Transactional
    @Override
    public ClientResponseDTO register(ClientRequestDTO request) {


        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("This email is already in use");
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodePassword(request.getPassword()))
                .role(Role.CLIENT)
                .build();

        User savedUser = userRepository.save(user);


        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .address(request.getAddress())
                .user(savedUser)
                .build();

        Client savedClient = clientRepository.save(client);


        return clientMapper.toResponseDTO(savedClient);
    }


    @Transactional(readOnly = true)
    @Override
    public ClientResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!checkPassword(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        Client client = clientRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        return clientMapper.toResponseDTO(client);
    }


    @Override
    public void logout(String sessionId) {
        sessionManager.invalidateSession(sessionId);
    }
    private String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(password.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error encoding password");
        }
    }
    private boolean checkPassword(String rawPassword, String encodedPassword) {
        return encodePassword(rawPassword).equals(encodedPassword);
    }
}
