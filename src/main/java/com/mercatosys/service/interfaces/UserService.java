package com.mercatosys.service.interfaces;

import com.mercatosys.dto.user.UserRequestDTO;
import com.mercatosys.dto.user.UserResponseDTO;
import com.mercatosys.dto.user.UserUpdateDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO create(UserRequestDTO dto);

    UserResponseDTO update(Long id, UserRequestDTO dto);

    void delete(Long id);

    UserResponseDTO getById(Long id);

    List<UserResponseDTO> getAll();
}
