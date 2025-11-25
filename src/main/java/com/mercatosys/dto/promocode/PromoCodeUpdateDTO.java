package com.mercatosys.dto.promocode;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PromoCodeUpdateDTO {

    private String code;
    private Double discountPercentage;
    private Double discountAmount;
    private LocalDateTime expirationDate;
    private Boolean active;
    private Double minimumOrderAmount;
    private Boolean deleted;
}
