package com.mose.agribora.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTO {
    private Long id;
    private String location;
    private String farmingType;
    private String specification;

    private Long userId;
    private String userName;
    private String userEmail;
    private String userPhoneNumber;
}
