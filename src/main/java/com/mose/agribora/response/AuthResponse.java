package com.mose.agribora.response;

import com.mose.agribora.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String role;
    private String username;
}
