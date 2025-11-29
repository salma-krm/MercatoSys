package com.mercatosys.service.impl;

import com.mercatosys.Exception.BusinessException;
import com.mercatosys.Exception.DuplicateResourceException;
import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.user.LoginRequestDTO;
import com.mercatosys.dto.user.LoginResponseDTO;
import com.mercatosys.dto.user.UserResponseDTO;
import com.mercatosys.entity.Client;
import com.mercatosys.entity.User;
import com.mercatosys.enums.CustomerLevel;
import com.mercatosys.enums.Role;
import com.mercatosys.mapper.AuthMapper;
import com.mercatosys.mapper.ClientMapper;
import com.mercatosys.mapper.UserMapper;
import com.mercatosys.repositories.ClientRepository;
import com.mercatosys.repositories.UserRepository;
import com.mercatosys.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Base64;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ClientMapper clientMapper;
    private final SessionManager sessionManager;
    private final UserMapper userMapper;

    // ------------------- REGISTER -------------------
    @Transactional
    @Override
    public ClientResponseDTO register(ClientRequestDTO request) {

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
        log.info("New client registered: {} ", savedClient.getId());

        return clientMapper.toResponseDTO(savedClient);
    }



    @Transactional(readOnly = true)
    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password"));


        if (!checkPassword(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Invalid email or password");
        }

        String sessionId = sessionManager.createSession(user);
        UserResponseDTO uDto = userMapper.toResponseDTO(user);
        return LoginResponseDTO.builder()
                .message("Connexion réussie")
                .user(uDto)
                .sessionId(sessionId)
                .build();
    }




    @Override
    public void logout(String sessionId) {
        sessionManager.invalidateSession(sessionId);
    }

    // ------------------- PASSWORD UTILS -------------------
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
