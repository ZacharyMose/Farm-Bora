package com.mose.agribora.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cropType;
    private String animalType;

    String location;

    private String role;
    private String farmingType;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
