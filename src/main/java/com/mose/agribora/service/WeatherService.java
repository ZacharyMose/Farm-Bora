package com.mose.agribora.service;
import com.mose.agribora.dto.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    @Value("${openweather.api.key}")
    private String API_KEY;

    public WeatherData getWeather(String location) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + location
                + "&appid=" + API_KEY
                + "&units=metric";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, WeatherData.class);
    }
}
