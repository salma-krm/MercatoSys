package com.mercatosys.dto.user;

import com.mercatosys.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserUpdateDTO {

    private String username;
    private String password;
    private String email;
    private Role role;
    private LocalDateTime updatedAt;
}
