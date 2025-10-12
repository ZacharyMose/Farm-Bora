package com.mose.agribora.dto;

import lombok.Data;

import java.util.List;

@Data
public class WeatherData {
    private Main main;
    private String rain;
    private Wind wind;
    private String name;  // city name

    @Data
    public static class Main {
        private double temp;
        private int humidity;
        @Data
        public static class Clouds {

        }
    }

    @Data
    public static class Weather {
        private String main;
        private String description;
    }

    @Data
    public static class Wind {
        private double speed;
    }
}
