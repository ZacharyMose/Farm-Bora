package com.mose.agribora.service;
import com.mose.agribora.dto.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private final String API_KEY = "286f71c112520be0609bdb6544bff920";

    public WeatherData getWeather(String location) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + location
                + "&appid=" + API_KEY
                + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, WeatherData.class);
    }
}
