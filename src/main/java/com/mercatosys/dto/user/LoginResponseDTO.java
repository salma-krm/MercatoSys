package com.mercatosys.dto.user;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {
    private String message;
    private UserResponseDTO user;
    private String sessionId;
}
