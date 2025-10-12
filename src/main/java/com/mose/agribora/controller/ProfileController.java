package com.mose.agribora.controller;

import com.mose.agribora.dto.ProfileDTO;
import com.mose.agribora.entity.Profile;
import com.mose.agribora.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ProfileDTO> createProfile(@PathVariable Long userId, @RequestBody Profile profile) {
        profileService.saveProfile(profile, userId);
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDTO> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(profileService.getProfileByUserId(userId));
    }
}
