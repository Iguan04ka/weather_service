package ru.iguana.weatherservicespringboot.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDtoWithCoordinates {
    private String name;

    private WeatherApiDto weather;

    private Float lat;

    private Float lon;
}
