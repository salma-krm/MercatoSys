package com.mercatosys.dto.client;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClientUpdateDTO {

    private String username;
    private String password;
    private String email;

    private String firstName;
    private String lastName;
    private String phone;
    private String address;
}
