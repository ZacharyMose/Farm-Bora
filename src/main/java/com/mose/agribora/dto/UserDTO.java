package com.mose.agribora.dto;

import com.mose.agribora.entity.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private Long id;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String password;
    private String phoneNumber;

    public UserDTO(String name, String email, String phoneNumber) {
        this.username = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
}
