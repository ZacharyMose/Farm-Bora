package com.mose.agribora.service;

import com.mose.agribora.dto.UserDTO;
import com.mose.agribora.entity.User;
import com.mose.agribora.request.ChangePasswordRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    UserDTO createUser(UserDTO dto); // Admin-only
    List<UserDTO> getAllUsers();
    Optional<User> findByEmail(String email);
    Optional<User> getUserById(Long id);
    void changePassword(String email , ChangePasswordRequest dto);
    void deleteUser(Long id);
}
