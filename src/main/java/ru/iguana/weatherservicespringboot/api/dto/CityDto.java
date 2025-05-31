package ru.iguana.weatherservicespringboot.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CityDto {
    private String name;

    private Float lat;

    private Float lon;

    private WeatherDto weather;

}
