package com.mose.agribora.request;

import com.mose.agribora.dto.WeatherData;
import com.mose.agribora.entity.Profile;
import lombok.Data;

@Data
public class AlertRequest {
    private String location;
    private WeatherData weather;
    private Profile profile;
}
