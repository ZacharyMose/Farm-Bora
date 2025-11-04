package com.mose.agribora.service;

import com.mose.agribora.dto.ProfileDTO;
import com.mose.agribora.entity.Profile;
import com.mose.agribora.entity.User;
import com.mose.agribora.repository.ProfileRepository;
import com.mose.agribora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public Profile createProfile(ProfileDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = Profile.builder()
                .cropType(request.getCropType())
                .animalType(request.getAnimalType())
                .location(request.getLocation())
                .role(request.getRole())
                .farmingType(request.getFarmingType())
                .user(user)
                .build();

        return profileRepository.save(profile);
    }

    public Profile getProfile(Long userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }
}
