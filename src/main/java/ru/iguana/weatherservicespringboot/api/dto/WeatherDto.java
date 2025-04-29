package ru.iguana.weatherservicespringboot.api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class WeatherDto {

    private Integer temperature;

    private Integer humidity;

    private Integer windSpeed;

    private LocalDateTime measuredAt;
}
