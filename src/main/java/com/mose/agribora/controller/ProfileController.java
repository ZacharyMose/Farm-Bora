package com.mose.agribora.controller;

import com.mose.agribora.dto.ProfileDTO;
import com.mose.agribora.entity.Profile;
import com.mose.agribora.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allow all origins
public class ProfileController {

    private final ProfileService profileService;

    // ✅ Create a new profile
    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody ProfileDTO request) {
        Profile profile = profileService.createProfile(request);
        return ResponseEntity.ok(profile);
    }

    // ✅ Get a profile by userId
    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        Profile profile = profileService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }
}
