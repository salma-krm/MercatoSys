package com.mercatosys.service.interfaces;

import com.mercatosys.dto.client.ClientRequestDTO;
import com.mercatosys.dto.client.ClientResponseDTO;
import com.mercatosys.dto.user.LoginRequestDTO;
import com.mercatosys.dto.user.LoginResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    public void  logout(String id);
    public LoginResponseDTO login(LoginRequestDTO request);
    public ClientResponseDTO register(ClientRequestDTO request);

}
