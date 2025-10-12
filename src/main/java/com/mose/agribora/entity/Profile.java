package com.mose.agribora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    @OneToOne
    private User user;

    private String farmingType; // type of farming i>e Crop or Animal

    private String specification; // specific type of farming if Abimal then specific, bees

}
