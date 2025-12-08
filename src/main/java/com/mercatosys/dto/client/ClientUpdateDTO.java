package com.mercatosys.dto.client;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientUpdateDTO {

    private String name;
    private String phone;
    private String address;
    private LocalDateTime updatedAt;
}
