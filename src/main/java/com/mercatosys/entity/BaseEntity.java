package com.mercatosys.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


    public BaseEntity() {
    }
}


