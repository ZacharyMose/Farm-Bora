package com.mose.agribora.controller;

import com.mose.agribora.dto.WeatherData;
import com.mose.agribora.entity.Profile;
import com.mose.agribora.repository.ProfileRepository;
import com.mose.agribora.service.GerminiService;
import com.mose.agribora.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/predict")
@RequiredArgsConstructor
public class PredictionController {
    private final WeatherService weatherService;
    private final ProfileRepository profileRepository;
    private final GerminiService germiniService;
    @GetMapping("/prediction")
    public ResponseEntity<String> predict(@RequestParam String location) {
        WeatherData weather = weatherService.getWeather(location);
        Profile profile = profileRepository.findById(1L).get();
        String prediction = germiniService.getPrediction(location, weather, profile);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping("/rearing-stages")
    public ResponseEntity<String> getRearingStages(@RequestParam String location) {
        WeatherData weather = weatherService.getWeather(location);
        Profile profile = profileRepository.findById(1L).get();
        String prediction = germiniService.getRearingStages(location,weather, profile);
        return ResponseEntity.ok(prediction);
    }

    @PostMapping("/alert")
    public String generateAlert(@RequestBody Map<String, Object> request) {
        String situation = (String) request.get("situation");
        String location = (String) request.get("location");

        // Optional: Build a sample profile (can be replaced with a real farmer profile from DB)
        Profile profile = Profile.builder()
                .farmingType((String) request.getOrDefault("farmingType", "Crop Farming"))
                .cropType((String) request.getOrDefault("cropType", "Maize"))
                .animalType((String) request.getOrDefault("animalType", "Crop Animal"))
                .build();

        return germiniService.generateSmartAlert( location, profile);
    }
}