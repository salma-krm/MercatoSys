package com.mercatosys.service.impl;

import com.mercatosys.Exception.ResourceNotFoundException;
import com.mercatosys.dto.user.UserRequestDTO;
import com.mercatosys.dto.user.UserResponseDTO;
import com.mercatosys.dto.user.UserUpdateDTO;
import com.mercatosys.entity.User;
import com.mercatosys.mapper.UserMapper;
import com.mercatosys.repositories.UserRepository;
import com.mercatosys.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO create(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        String encodedPassword = encodePassword(dto.getPassword());
        user.setPassword(encodedPassword);

        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }


    @Override
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable avec l'id: " + id));
        userMapper.updateEntityFromDTO(dto,user);
        user = userRepository.save(user);
        return userMapper.toResponseDTO(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User introuvable avec l'id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User introuvable avec l'id: " + id));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
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
