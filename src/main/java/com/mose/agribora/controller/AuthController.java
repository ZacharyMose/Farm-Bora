package com.mose.agribora.controller;

import com.mose.agribora.request.AuthRequest;
import com.mose.agribora.response.AuthResponse;
import com.mose.agribora.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authenticationRequest) {
        logger.debug("Authenticating user: {}", authenticationRequest.getEmail());
        logger.info("Authenticating user: {}", authenticationRequest.getPassword());
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
