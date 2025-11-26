package com.mercatosys.dto.client;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private Long userId; // <-- correspond à Client.user.id
}
