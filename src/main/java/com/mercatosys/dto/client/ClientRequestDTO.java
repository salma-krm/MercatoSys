package com.mercatosys.dto.client;

import com.mercatosys.dto.user.UserRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientRequestDTO {



    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    private String phone;
    private String address;

    @Valid
    @NotNull(message = "Les informations utilisateur sont obligatoires")
    private UserRequestDTO user;
}
