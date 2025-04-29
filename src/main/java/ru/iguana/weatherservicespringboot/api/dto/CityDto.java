package ru.iguana.weatherservicespringboot.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {
    private Integer id;

    private String name;

    private WeatherDto weather;

}
