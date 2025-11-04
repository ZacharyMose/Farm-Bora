package com.mose.agribora.controller;

import com.mose.agribora.entity.Sensors;
import com.mose.agribora.service.SensorDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataService sensorDataService;

    // 🔹 Save a new sensor reading
    @PostMapping
    public ResponseEntity<Sensors> saveSensorData(@RequestBody Sensors data) {
        Sensors savedData = sensorDataService.saveSensorData(data);
        return ResponseEntity.ok(savedData);
    }

    // 🔹 Get all sensor readings
    @GetMapping
    public ResponseEntity<List<Sensors>> getAllSensorData() {
        List<Sensors> allData = sensorDataService.getAllSensorData();
        return ResponseEntity.ok(allData);
    }

    // 🔹 Get all sensor readings by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Sensors>> getSensorDataByLocation(@PathVariable String location) {
        List<Sensors> readings = sensorDataService.getSensorDataByLocation(location);
        return ResponseEntity.ok(readings);
    }

    // 🔹 Get the latest reading by location
    @GetMapping("/latest/{location}")
    public ResponseEntity<Sensors> getLatestSensorData(@PathVariable String location) {
        Sensors latest = sensorDataService.getLatestSensorData(location);
        return ResponseEntity.ok(latest);
    }
}
