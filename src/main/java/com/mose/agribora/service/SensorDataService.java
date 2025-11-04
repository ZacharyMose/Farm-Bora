package com.mose.agribora.service;

import com.mose.agribora.entity.Sensors;
import com.mose.agribora.repository.SensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    public Sensors saveSensorData(Sensors data) {
        data.setTimestamp(LocalDateTime.now());
        return sensorDataRepository.save(data);
    }

    public List<Sensors> getAllSensorData() {
        return sensorDataRepository.findAll();
    }

    public List<Sensors> getSensorDataByLocation(String location) {
        return sensorDataRepository.findByLocationOrderByTimestampDesc(location);
    }

    public Sensors getLatestSensorData(String location) {
        List<Sensors> readings = getSensorDataByLocation(location);
        return readings.isEmpty() ? null : readings.get(0);
    }
}
