package com.mose.agribora.controller;

import com.mose.agribora.dto.WeatherData;
import com.mose.agribora.entity.Profile;
import com.mose.agribora.repository.ProfileRepository;
import com.mose.agribora.service.GerminiService;
import com.mose.agribora.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/predict")
@RequiredArgsConstructor
public class PredictionController {
    private final WeatherService weatherService;
    private final ProfileRepository profileRepository;
    private final GerminiService germiniService;
    @GetMapping
    public ResponseEntity<String> predict(@RequestParam String location) {
        WeatherData weather = weatherService.getWeather(location);
        Profile profile = profileRepository.findById(1L).get();
        String prediction = germiniService.getPrediction(location, weather, profile);
        return ResponseEntity.ok(prediction);
    }
}