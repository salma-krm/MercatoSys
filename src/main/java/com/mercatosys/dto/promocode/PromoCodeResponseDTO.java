package com.mercatosys.dto.promocode;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PromoCodeResponseDTO {

    private Long id;
    private String code;
    private boolean deleted;
    private Double discountPercentage;
    private Double discountAmount;
    private LocalDateTime expirationDate;
    private boolean active;
    private Double minimumOrderAmount;
}
