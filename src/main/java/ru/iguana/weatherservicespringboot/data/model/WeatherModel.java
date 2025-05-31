package ru.iguana.weatherservicespringboot.data.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherModel {
    @JsonAlias("dt_forecast")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime forecastDate;

    @JsonAlias("temp_100_cel")
    private Float temperature;

    @JsonAlias("temp_feels_cel")
    private Float feelingTemperature;

    @JsonAlias("wind_speed_10")
    private Float windSpeed;

    @JsonAlias("vlaga_2")
    private Float humidity;
}
