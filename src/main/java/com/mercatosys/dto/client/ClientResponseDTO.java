package com.mercatosys.dto.client;

import com.mercatosys.enums.CustomerLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseDTO {

    private Long id;                // ID du client

    private String name;        // Nom
    private String phone;           // Téléphone
    private String address;         // Adresse
    private Long userId;            // ID du User associé
    private int totalOrder;         // Nombre total de commandes
    private double totalSpent;      // Total dépensé par le client
    private CustomerLevel level;    // Niveau du client (BASIC, SILVER, GOLD, PLATINUM)
}
