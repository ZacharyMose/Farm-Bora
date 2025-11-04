package com.mose.agribora.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlertEvent {
    User user = new User();
    Sensors sensors = new Sensors();
    private String farmerEmail = user.getEmail();
    private Double temperature = sensors.getTemperature();
    private Double humidity =  sensors.getHumidity();
    private String farmerPhone = user.getPhoneNumber();
}
