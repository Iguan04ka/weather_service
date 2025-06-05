package ru.iguana.weatherservicespringboot.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import ru.iguana.weatherservicespringboot.data.model.WeatherModel;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Schema(description = "DTO, представляющий город и его погодные данные")
public class CityDto {

    @Schema(description = "Название города", example = "Москва")
    private String name;

    @Schema(description = "Широта", example = "55.7558")
    private Float lat;

    @Schema(description = "Долгота", example = "37.6173")
    private Float lon;

    @Schema(description = "Погодные данные города")
    private WeatherDto weather;
}
