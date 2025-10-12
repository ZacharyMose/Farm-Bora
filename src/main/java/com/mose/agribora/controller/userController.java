package com.mose.agribora.controller;

import com.mose.agribora.dto.UserDTO;
import com.mose.agribora.repository.UserRepository;
import com.mose.agribora.request.ChangePasswordRequest;
import com.mose.agribora.service.JwtService;
import com.mose.agribora.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class userController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal) {
        // principal.getName() gives logged-in user's email (from JWT subject)
        userService.changePassword(principal.getName(), request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }


}
