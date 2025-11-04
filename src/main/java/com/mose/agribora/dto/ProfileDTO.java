package com.mose.agribora.dto;

import com.mose.agribora.entity.Location;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {
    private String cropType;
    private String animalType;
    private String location;
    private String role;
    private String farmingType;
    private Long userId;
}
