package ru.iguana.weatherservicespringboot.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherApiDto {
    @JsonAlias("dt_forecast")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime forecastDate;

    @JsonAlias("temp_2_cel")
    private Float temperature;

    @JsonAlias("temp_feels_cel")
    private Float feelingTemperature;

    @JsonAlias("wind_speed_10")
    private Float windSpeed;

    @JsonAlias("vlaga_2")
    private Float humidity;
}
