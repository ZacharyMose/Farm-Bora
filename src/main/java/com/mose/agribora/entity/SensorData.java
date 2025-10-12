package com.mose.agribora.entity;

import jakarta.persistence.*;
import org.springframework.aop.target.ThreadLocalTargetSourceStats;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensors")
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double temperature;
    private Double humidity;
    private Double soilMoisture;
    private Double rainfallAmount;
    private LocalDateTime timestamp;
}
