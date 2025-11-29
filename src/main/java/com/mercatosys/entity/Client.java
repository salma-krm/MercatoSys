package com.mercatosys.entity;

import com.mercatosys.enums.CustomerLevel;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String address;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CustomerLevel level = CustomerLevel.BASIC;

    @Builder.Default
    private int totalOrder = 0;

    @Builder.Default
    private double totalSpent = 0.0;

}
