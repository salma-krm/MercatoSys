package com.mercatosys.service;

import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.user.LoginRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    public void  logout(String id);
    public ClientResponseDTO login(LoginRequestDTO request);
    public ClientResponseDTO register(ClientRequestDTO request);

}
