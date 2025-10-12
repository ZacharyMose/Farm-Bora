package com.mose.agribora.service;

import com.mose.agribora.dto.ProfileDTO;
import com.mose.agribora.entity.Profile;
import com.mose.agribora.entity.User;
import com.mose.agribora.repository.ProfileRepository;
import com.mose.agribora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public Profile saveProfile(Profile profile, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        profile.setUser(user);
        return profileRepository.save(profile);
    }

    public ProfileDTO getProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Profile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + userId));
        if (profile == null) {
            throw new RuntimeException("Profile not found for user ID: " + userId);
        }

        return mapToDto(profile);
    }

    private ProfileDTO mapToDto(Profile profile) {
        User user = profile.getUser();
        return ProfileDTO.builder()
                .id(profile.getId())
                .location(profile.getLocation())
                .farmingType(profile.getFarmingType())
                .specification(profile.getSpecification())
                .userId(user.getId())
                .userName(user.getUsername())
                .userEmail(user.getEmail())
                .userPhoneNumber(user.getPhoneNumber())
                .build();
    }
}
